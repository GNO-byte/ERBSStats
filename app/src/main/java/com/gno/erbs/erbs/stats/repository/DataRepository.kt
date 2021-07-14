package com.gno.erbs.erbs.stats.repository

import android.content.Context
import com.gno.erbs.erbs.stats.model.CharacterStats
import com.gno.erbs.erbs.stats.model.TeamMode
import com.gno.erbs.erbs.stats.model.aesop.item.Item
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.model.erbs.characters.CharacterLevelUpStat
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.gno.erbs.erbs.stats.model.erbs.matches.item.armor.ItemArmor
import com.gno.erbs.erbs.stats.model.erbs.matches.item.special.ItemSpecial
import com.gno.erbs.erbs.stats.model.erbs.matches.item.weapon.ItemWeapon
import com.gno.erbs.erbs.stats.model.erbs.nickname.User
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank
import com.gno.erbs.erbs.stats.model.erbs.userstats.CharacterStat
import com.gno.erbs.erbs.stats.model.erbs.userstats.UserStats
import com.gno.erbs.erbs.stats.repository.drive.CharacterImageType
import com.gno.erbs.erbs.stats.repository.drive.DriveService
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object DataRepository {

    private val aesopService = AesopService()
    private var erbsService: ERBSService? = null
    private var driveService: DriveService? = null
    private val defaultValue = DefaultValue

    operator fun invoke(context: Context): DataRepository {
        erbsService = ERBSService(context)
        driveService = DriveService(context)
        return this
    }

    fun initCoreDate() {
        driveService?.initImagesLinkStructure()
        driveService?.initCoreCharacters()
    }

    fun setDefaultValues(context: Context) {
        DefaultValue
            .setDefaultValues(context)
    }

    fun getDefaultMatchingTeamMode(context: Context): String {
        return DefaultValue
            .getMatchingTeamMode(context)
    }

    fun getDefaultSeasonId(context: Context): String {
        return defaultValue.getSeasonId(context)
    }

    fun getDefaultUserNumber(context: Context): String {
        return defaultValue.getUserNumber(context)
    }

    suspend fun getItem(itemId: Int): Item {
        return aesopService.getItem(itemId)[0]
    }

    suspend fun getCharacters(withIconWebLink: Boolean = false): List<Character> {

        val characters = erbsService?.getCharacters()?.result ?: listOf()
        if (withIconWebLink) addIconWebLink(characters)
        return characters

    }

    private fun addIconWebLink(characterList: List<Character>) {

        var names = mutableListOf<String>()

        characterList.forEach {
            names.add(it.name)
        }
        val files =
            DriveService.getCharacterImageFiles(CharacterImageType.MINI, *names.toTypedArray())

        characterList.forEach { character ->
            character.iconWebLink = files.first { file ->
                file.name.contains(character.name, ignoreCase = true)
            }.webContentLink
        }
    }

    suspend fun getUserStats(
        userNumber: String,
        seasonId: String,
        characters: List<Character>
    ): List<UserStats> {
        val userStats = erbsService?.getUserStatsResponse(
            userNumber,
            seasonId
        )?.result ?: listOf()
        addAdditionalParamCharacterStat(userStats, characters)
        return userStats
    }

    private fun addAdditionalParamCharacterStat(
        userStats: List<UserStats>,
        characters: List<Character>
    ) {

        userStats.forEach { userStatsList ->
            userStatsList.characterStats.forEach { characterStats ->
                try {
                    characterStats.characterName =
                        characters.first { it.code == characterStats.characterCode }.name
                    characterStats.matchingTeamMode = userStatsList.matchingTeamMode
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

        addRankTierImage(userStats)
        addTopCharacterImage(userStats)

    }

    private fun addTopCharacterImage(userStats: List<UserStats>) {

        val characterHalfFiles = driveService?.getCharacterHalfFiles()
        userStats.forEach { userStat ->
            val topCharacterName = userStat.characterStats.maxByOrNull { it.usages }?.characterName

            topCharacterName?.let { characterName ->
                userStat.topCharacterHalfImageWebLink = characterHalfFiles?.first { driveFile ->
                    driveService?.compareNames(
                        driveFile.name,
                        characterName
                    ) ?: false
                }?.webContentLink
            }

        }
    }

    private fun addRankTierImage(userStats: List<UserStats>) {

        val searchTierRankImageMap = mutableMapOf<String, String?>()
        val mmrSet = mutableSetOf<Int>()

        userStats.forEach { corUserStats ->

            val roundedMmr = corUserStats.mmr / 100 * 100

            val searchName = when {
                roundedMmr < 400 -> "Iron"
                roundedMmr < 800 -> "Bronze"
                roundedMmr < 1200 -> "Silver"
                roundedMmr < 1600 -> "Gold"
                roundedMmr < 2000 -> "Platinum"
                roundedMmr < 2400 -> "Diamon"
                roundedMmr < 2800 -> "Titan"
                roundedMmr < 2800 -> "Titan"
                roundedMmr >= 2800 -> "Immortal"
                else -> "Unrank"
            }

            mmrSet += roundedMmr
            searchTierRankImageMap += searchName to null
        }

        val files = DriveService.getRankTierFile(*mmrSet.toIntArray())

        searchTierRankImageMap.forEach { mapEntry ->
            searchTierRankImageMap[mapEntry.key] = files.first { driveFile ->
                driveService?.compareNames(driveFile.name, mapEntry.key) ?: false
            }.webContentLink

        }

        userStats.forEach { corUserStats ->


            val searchNameMmr = when {
                corUserStats.mmr < 400 -> "Iron"
                corUserStats.mmr < 800 -> "Bronze"
                corUserStats.mmr < 1200 -> "Silver"
                corUserStats.mmr < 1600 -> "Gold"
                corUserStats.mmr < 2000 -> "Platinum"
                corUserStats.mmr < 2400 -> "Diamon"
                corUserStats.mmr < 2800 -> "Titan"
                corUserStats.mmr < 2800 -> "Titan"
                corUserStats.mmr >= 2800 -> "Immortal"
                else -> "Unrank"
            }
            corUserStats.rankTierImageWebLink = searchTierRankImageMap[searchNameMmr]

        }
    }

    fun getUserCharactersStats(userStats: List<UserStats>): List<CharacterStat> {
        val charactersStats: MutableList<CharacterStat> = arrayListOf()
        userStats.forEach {
            charactersStats.addAll(it.characterStats)
        }
        addImageCharacterStats(charactersStats)
        return charactersStats
    }

    private fun addImageCharacterStats(characterStats: MutableList<CharacterStat>) {

        val characterSearchMap = mutableMapOf<String, String?>()

        characterStats.forEach { characterStats ->
            characterStats.characterName?.let {
                characterSearchMap += it to null
            }
        }

        val files = DriveService.getCharacterImageFiles(
            CharacterImageType.MINI,
            *characterSearchMap.keys.toTypedArray()
        )

        characterSearchMap.forEach { map ->
            characterSearchMap[map.key] = files.first { driveFile ->
                driveService?.compareNames(driveFile.name, map.key) ?: false
            }.webContentLink
        }

        characterStats.forEach { characterStats ->
            characterStats.WebLinkImage = characterSearchMap[characterStats.characterName]
        }
    }

    suspend fun getUserGames(
        userNumber: String,
        userStats: List<UserStats>,
    ): List<UserGame> {

        val userGames = erbsService?.getUserGamesResponse(userNumber)?.result ?: listOf()
        addAdditionalParamUserGames(userGames, userStats[0].mmr)

        addCharacterWebLink(userGames)
        addItemWeaponTypeWebLink(userGames)
        addItemWebLink(userGames)

        return userGames.toList()
    }


    private suspend fun addCharacterWebLink(userGames: List<UserGame>) {

        val characters = getCharacters(true)

        userGames.forEach { userGame ->
            userGame.characterImageWebLink =
                characters.first { userGame.characterNum == it.code }.iconWebLink
        }
    }

    private suspend fun addItemWeaponTypeWebLink(userGames: List<UserGame>) {
        val itemsWeapon = erbsService?.getItemsWeapon()?.result
        val itemWeaponImage = driveService?.getWeaponTypeFiles()

        val equipmentList = mutableListOf<Int>()

        userGames.forEach { userGame ->

            equipmentList += userGame.equipment.item1Id
            equipmentList += userGame.equipment.item2Id
            equipmentList += userGame.equipment.item3Id
            equipmentList += userGame.equipment.item4Id
            equipmentList += userGame.equipment.item5Id
            equipmentList += userGame.equipment.item6Id

            var findType = ""
            equipmentList.takeWhile { findType == "" }.forEach {
                try {
                    findType = itemsWeapon?.first { itemWeapon ->
                        itemWeapon.code == it
                    }?.weaponType ?: ""
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            if (findType == "") {
                userGame.weaponTypeImageWebLink = null
            } else {
                try {
                    userGame.weaponTypeImageWebLink = itemWeaponImage?.first { driveFile ->
                        driveService?.compareNames(driveFile.name, findType) ?: false
                    }?.webContentLink
                } catch (e: Exception) {
                    Timber.e(e)
                }

            }
            equipmentList.clear()
        }
    }

    suspend fun addItemWebLink(userGames: List<UserGame>) {

        val equipmentLinks = mutableMapOf<Int, String?>()

        userGames.forEach { userGame ->
            equipmentLinks += userGame.equipment.item1Id to null
            equipmentLinks += userGame.equipment.item2Id to null
            equipmentLinks += userGame.equipment.item3Id to null
            equipmentLinks += userGame.equipment.item4Id to null
            equipmentLinks += userGame.equipment.item5Id to null
            equipmentLinks += userGame.equipment.item6Id to null
        }

        fillItemsWeapon(equipmentLinks)
        fillItemsArmor(equipmentLinks)
        fillItemsSpecial(equipmentLinks)

        userGames.forEach { userGame ->
            userGame.equipment.item1WebLink = equipmentLinks[userGame.equipment.item1Id] ?: ""
            userGame.equipment.item2WebLink = equipmentLinks[userGame.equipment.item2Id] ?: ""
            userGame.equipment.item3WebLink = equipmentLinks[userGame.equipment.item3Id] ?: ""
            userGame.equipment.item4WebLink = equipmentLinks[userGame.equipment.item4Id] ?: ""
            userGame.equipment.item5WebLink = equipmentLinks[userGame.equipment.item5Id] ?: ""
            userGame.equipment.item6WebLink = equipmentLinks[userGame.equipment.item6Id] ?: ""

        }

    }

    private suspend fun fillItemsWeapon(equipmentLinks: MutableMap<Int, String?>) {

        val itemsWeapon = erbsService?.getItemsWeapon()?.result
        val foundItems = mutableListOf<ItemWeapon>()

        equipmentLinks.forEach { equipmentLink ->
            try {
                itemsWeapon?.let { thisItemsWeapon ->
                    foundItems.add(thisItemsWeapon.first { itemWeapon -> itemWeapon.code == equipmentLink.key })

                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        val foundItemsType = foundItems.map {
            it.weaponType
        }.toSet().filterNotNull()

        foundItemsType.forEach { foundItemType ->
            var driveIdWeapon: String? = null
            try {
                driveIdWeapon = driveService?.imagesLinkStructure?.weapon?.first { weapon ->
                    weapon.weaponType == foundItemType
                }?.id

            } catch (e: java.lang.Exception) {
                Timber.e(e)
            }

            driveIdWeapon?.let { id ->
                val folderfiles = driveService?.getFolderFiles(id)
                val foundItemsWithType = foundItems.filter { foundItem ->
                    foundItem.weaponType == foundItemType
                }

                foundItemsWithType.forEach { foundItemWithType ->
                    foundItemWithType.name?.let { nameItem ->
                        foundItemWithType.code?.let { code ->
                            try {
                                val v = foundItemType
                                val v2 = driveIdWeapon
                                val findDriveFile = folderfiles?.first { driveFile ->
                                    driveFile.name.contains(nameItem, ignoreCase = true)
                                }
                                equipmentLinks += code to findDriveFile?.webContentLink
                                val v3 = equipmentLinks
                            } catch (e: Exception) {
                                Timber.e(e)
                            }

                        }

                    }
                }

            }
        }

    }

    private suspend fun fillItemsArmor(equipmentLinks: MutableMap<Int, String?>) {

        val itemsArmor = erbsService?.getItemsArmor()?.result //TODO
        val foundItems = mutableListOf<ItemArmor>() //TODO

        equipmentLinks.forEach { equipmentLink ->
            try {
                itemsArmor?.let { thisItemArmor ->
                    foundItems.add(thisItemArmor.first { itemsArmor -> itemsArmor.code == equipmentLink.key }) //TODO

                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        val foundItemsType = foundItems.map {
            it.armorType   //TODO
        }.toSet().filterNotNull()

        foundItemsType.forEach { foundItemType ->
            var driveIdArmor: String? = null
            try {
                driveIdArmor = driveService?.imagesLinkStructure?.armor?.first { armor -> //TODO
                    armor.armorType == foundItemType //TODO
                }?.id

            } catch (e: java.lang.Exception) {
                Timber.e(e)
            }

            driveIdArmor?.let { id ->
                val folderfiles = driveService?.getFolderFiles(id)
                val foundItemsWithType = foundItems.filter { foundItem ->
                    foundItem.armorType == foundItemType //TODO
                }

                foundItemsWithType.forEach { foundItemWithType ->
                    foundItemWithType.name?.let { nameItem ->
                        foundItemWithType.code?.let { code ->
                            try {
                                val v = foundItemType
                                val v2 = driveIdArmor
                                val findDriveFile = folderfiles?.first { driveFile ->
                                    driveFile.name.contains(nameItem, true)
                                            || driveFile.name.replace(" ", "")
                                        .contains(nameItem.replace(" ", ""), true)
                                }
                                equipmentLinks += code to findDriveFile?.webContentLink
                                val v3 = equipmentLinks
                            } catch (e: Exception) {
                                Timber.e(e)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun fillItemsSpecial(equipmentLinks: MutableMap<Int, String?>) {

        val itemsSpecial = erbsService?.getItemsSpecial()?.result //TODO
        val foundItems = mutableListOf<ItemSpecial>() //TODO

        equipmentLinks.forEach { equipmentLink ->
            try {
                itemsSpecial?.let { thisItemSpecial ->
                    foundItems.add(thisItemSpecial.first { itemsSpecial -> itemsSpecial.code == equipmentLink.key }) //TODO
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        val foundItemsGrade = foundItems.map {//TODO
            it.itemGrade   //TODO//TODO
        }.toSet().filterNotNull()

        foundItemsGrade.forEach { foundItemGrade ->//TODO
            var driveIdSpecial: String? = null//TODO
            try {
                driveIdSpecial =
                    driveService?.imagesLinkStructure?.special?.first { special -> //TODO
                        special.itemGrade == foundItemGrade //TODO
                    }?.id

            } catch (e: java.lang.Exception) {
                Timber.e(e)
            }

            driveIdSpecial?.let { id ->//TODO
                val folderfiles = driveService?.getFolderFiles(id)
                val foundItemsWithType = foundItems.filter { foundItem ->
                    foundItem.itemGrade == foundItemGrade //TODO
                }

                foundItemsWithType.forEach { foundItemWithType ->
                    foundItemWithType.name?.let { nameItem ->
                        foundItemWithType.code?.let { code ->
                            try {
                                val v = foundItemGrade
                                val v2 = driveIdSpecial
                                val findDriveFile = folderfiles?.first { driveFile ->
                                    driveFile.name.contains(nameItem, ignoreCase = true)
                                }
                                equipmentLinks += code to findDriveFile?.webContentLink
                                val v3 = equipmentLinks
                            } catch (e: Exception) {
                                Timber.e(e)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addAdditionalParamUserGames(userGames: List<UserGame>, lastMmr: Int) {

        for (index in userGames.indices) {
            if (index != 0) userGames[index].mmr = userGames[index - 1].mmrBefore
            else userGames[index].mmr = lastMmr
            userGames[index].teamMode =
                TeamMode.findByValue(userGames[index].matchingTeamMode)
            userGames[index].date = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                Locale.getDefault()
            ).parse(userGames[index].startDtm) ?: Date()

            userGames[index].equipment.item1WebLink = ""
            userGames[index].equipment.item2WebLink = ""
            userGames[index].equipment.item3WebLink = ""
            userGames[index].equipment.item4WebLink = ""
            userGames[index].equipment.item5WebLink = ""
            userGames[index].equipment.item6WebLink = ""
        }

    }

    suspend fun getCharacter(code: Int): Character? {
        return erbsService?.getCharacters()?.result?.find { it.code == code }
    }

    suspend fun getCharacter(name: String): List<Character>? {
        return erbsService?.getCharacters()?.result?.filter { character ->
            driveService?.compareNames(character.name, name) ?: (character.name == name)
        }
    }

    suspend fun getCharacterWeaponTypes(code: Int): List<WeaponType> {
        val weaponTypes =
            erbsService?.getCharacterWeaponTypes()?.result?.filter { it.characterCode == code }
        val weaponTypeFiles = driveService?.getWeaponTypeFiles()

        weaponTypes?.forEach { weaponType ->
            weaponType.mastery?.let { mastery ->
                try {
                    weaponType.weaponTypeImageWebLink = weaponTypeFiles?.first { driveFile ->
                        driveService?.compareNames(driveFile.name, mastery) ?: false
                    }?.webContentLink
                } catch (e: java.lang.Exception) {
                    Timber.e(e)
                }
            }
        }

        return weaponTypes ?: listOf()
    }

    suspend fun getCharacterLevelUpStat(code: Int): CharacterLevelUpStat? {
        return erbsService?.getCharacterLevelUpStat()?.result?.first { it.code == code }
    }

    suspend fun getTopRanks(seasonId: String, teamMode: String): List<Rank> {
        val ranks = erbsService
            ?.getTopRanksResponse(seasonId, teamMode)?.result?.toList() ?: listOf()
        addRankTierImage(ranks)
        return ranks
    }

    @JvmName("addRankTierImageTopRank")
    private fun addRankTierImage(ranks: List<Rank>) {

        val searchTierRankImageMap = mutableMapOf<String, String?>()
        val mmrSet = mutableSetOf<Int>()

        ranks.forEach { corUserStats ->

            val roundedMmr = corUserStats.mmr / 100 * 100

            val searchName = when {
                roundedMmr < 400 -> "Iron"
                roundedMmr < 800 -> "Bronze"
                roundedMmr < 1200 -> "Silver"
                roundedMmr < 1600 -> "Gold"
                roundedMmr < 2000 -> "Platinum"
                roundedMmr < 2400 -> "Diamon"
                roundedMmr < 2800 -> "Titan"
                roundedMmr < 2800 -> "Titan"
                roundedMmr >= 2800 -> "Immortal"
                else -> "Unrank"
            }

            mmrSet += roundedMmr
            searchTierRankImageMap += searchName to null
        }

        val files = DriveService.getRankTierFile(*mmrSet.toIntArray())

        searchTierRankImageMap.forEach { mapEntry ->
            searchTierRankImageMap[mapEntry.key] = files.first { driveFile ->
                driveService?.compareNames(driveFile.name, mapEntry.key) ?: false
            }.webContentLink

        }

        ranks.forEach { corUserStats ->

            val searchNameMmr = when {
                corUserStats.mmr < 400 -> "Iron"
                corUserStats.mmr < 800 -> "Bronze"
                corUserStats.mmr < 1200 -> "Silver"
                corUserStats.mmr < 1600 -> "Gold"
                corUserStats.mmr < 2000 -> "Platinum"
                corUserStats.mmr < 2400 -> "Diamon"
                corUserStats.mmr < 2800 -> "Titan"
                corUserStats.mmr < 2800 -> "Titan"
                corUserStats.mmr >= 2800 -> "Immortal"
                else -> "Unrank"
            }
            corUserStats.rankTierImageWebLink = searchTierRankImageMap[searchNameMmr]

        }
    }

    fun addCharacterHalfWebLink(characterStats: CharacterStats) {

        val characterHalfFiles = driveService?.getCharacterHalfFiles()
        characterStats.characterImageHalfWebLink = characterHalfFiles?.first { driveFile ->
            driveService?.compareNames(
                driveFile.name,
                characterStats.name
            ) ?: false
        }?.webContentLink

    }

    fun getCoreCharacter(code: Int): CoreCharacter? {
        return driveService?.coreCharacters?.find { it.code == code }
    }

    suspend fun getSkills(code: Int): List<CoreSkill>? {
        val character = getCoreCharacter(code)

        character?.skills?.let { coreSkills ->
            character.name?.let { addSkillsImageLink(coreSkills, it) }
            addSkillsVideoLink(coreSkills)
        }

        return character?.skills
    }

    private fun addSkillsImageLink(coreSkills: List<CoreSkill>, nameCharacter: String) {

        driveService?.let { thisDriveService ->
            val imageWebLinks = thisDriveService.getSkillImage(nameCharacter)

            coreSkills.forEach { coreSkill ->
                coreSkill.key?.let { key ->
                    coreSkill.image = imageWebLinks?.find { driveFile ->
                        thisDriveService.compareNames(
                            driveFile.name,
                            key
                        )
                    }?.webContentLink
                }
            }
        }
    }

    private suspend fun addSkillsVideoLink(coreSkills: List<CoreSkill>) {

        erbsService?.let { thisErbsService ->
            val skillVideo = thisErbsService.getCharacterSkillVideos().result
            coreSkills.forEach { coreSkill ->
                coreSkill.videoLink = skillVideo.find { it.code == coreSkill.group }?.youTubeUrl
            }
        }

    }

    suspend fun getUser(searchString: String): List<User> {

        var foundUsers = mutableListOf<User>()

        try{
            erbsService?.getUserResponse(searchString)?.result?.let { foundUsers.add(it) }
        } catch (e: Exception){

        }

        return foundUsers

    }

}