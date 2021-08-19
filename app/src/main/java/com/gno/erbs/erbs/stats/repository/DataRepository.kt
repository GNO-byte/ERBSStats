package com.gno.erbs.erbs.stats.repository

import android.content.Context
import com.gno.erbs.erbs.stats.model.CharacterStats
import com.gno.erbs.erbs.stats.model.TeamMode
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.model.erbs.Response
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.model.erbs.characters.CharacterLevelUpStat
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.gno.erbs.erbs.stats.model.erbs.matches.item.SearchItem
import com.gno.erbs.erbs.stats.model.erbs.nickname.User
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank
import com.gno.erbs.erbs.stats.model.erbs.userstats.CharacterStat
import com.gno.erbs.erbs.stats.model.erbs.userstats.UserStats
import com.gno.erbs.erbs.stats.repository.drive.CharacterImageType
import com.gno.erbs.erbs.stats.repository.room.Converter
import com.gno.erbs.erbs.stats.repository.room.RoomService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resumeWithException
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

object DataRepository {

    private val aesopService = AesopService()
    private var erbsService: ERBSService? = null
    private var imageService: ImageService = FirebaseService
    private val defaultValue = DefaultValue
    private var roomService: RoomService? = null

    suspend operator fun invoke(context: Context): DataRepository {
        erbsService = ERBSService(context)
        roomService = RoomService(context)

        initData(context)

        return this
    }

    private suspend fun initData(context: Context) {

        val dataVersion = DefaultValue.getDataVersion(context)
        val exchangeDataVersion = imageService.getDataVersion()
        if (dataVersion != exchangeDataVersion) {
            DefaultValue.setDataVersion(context, exchangeDataVersion ?: 0)
            updateCharacters()
            updateCoreCharacters()
        }
    }

    private suspend fun updateCharacters() {

        val characters = erbsService?.getCharacters()?.result ?: listOf()
        addIconWebLink(characters, "iconWebLink", "name")

        roomService?.addCharacters(characters.map {
            Converter.conv(it)
        })

    }

    private fun updateCoreCharacters() {

        roomService?.let { thisRoomService ->
            imageService.coreCharacters?.mapNotNull { Converter.conv(it) }?.let {
                thisRoomService.addCoreCharacters(it)
            }
        }
    }


    fun setDefaultValues(context: Context) {
        defaultValue
            .setDefaultValues(context)
    }

    fun getDefaultMatchingTeamMode(context: Context): String {
        return defaultValue
            .getMatchingTeamMode(context)
    }

    fun setDefaultMatchingTeamMode(context: Context, value: String) {
        defaultValue.setMatchingTeamMode(context, value)
    }

    fun getDefaultSeasonId(context: Context): String {
        return defaultValue.getSeasonId(context)
    }

    fun setDefaultSeasonId(context: Context, value: String) {
        defaultValue.setSeasonId(context, value)
    }

    fun getDefaultUserNumber(context: Context): String {
        return defaultValue.getUserNumber(context)
    }

    fun setDefaultUserNumber(context: Context, value: String) {
        defaultValue.setUserNumber(context, value)
    }


    fun getCharacters(): List<Character> {
        var roomCharacters = roomService?.getCharacters()
        return roomCharacters?.let { thisRoomCharacters ->
            thisRoomCharacters.map { Converter.conv(it) }
        } ?: listOf()
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

    private suspend fun addAdditionalParamCharacterStat(
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

    private suspend fun addTopCharacterImage(userStats: List<UserStats>) {

        imageService.let { thisImageService ->
            userStats.forEach { userStat ->
                val topCharacterName =
                    userStat.characterStats.maxByOrNull {
                        it.usages ?: 0
                    }?.characterName

                topCharacterName?.let { characterName ->
                    userStat.topCharacterHalfImageWebLink = thisImageService.getCharacterImageFiles(
                        CharacterImageType.HALF,
                        characterName
                    )?.getWebLink(roomService)
                }

            }
        }

    }

    private suspend fun addRankTierImage(userStats: List<UserStats>) {

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

        val files = imageService.getRankTierFile(*mmrSet.toIntArray())

        searchTierRankImageMap.forEach { mapEntry ->
            searchTierRankImageMap[mapEntry.key] = files.find { driveFile ->
                imageService.compareNames(driveFile.name, mapEntry.key) ?: false
            }?.getWebLink(roomService)

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

    suspend fun getUserCharactersStats(userStats: List<UserStats>): List<CharacterStat> {
        val charactersStats: MutableList<CharacterStat> = arrayListOf()
        userStats.forEach {
            charactersStats.add(
                CharacterStat(
                    isHead = true,
                    headName = TeamMode.findByValue(it.matchingTeamMode).title
                )
            )
            charactersStats.addAll(it.characterStats)
        }

        addIconWebLink(charactersStats, "WebLinkImage", "characterName")
        return charactersStats
    }


    suspend fun getUserGames(
        userNumber: String,
        mmr: Int,
        nextPage: String
    ): Response<List<UserGame>>? {

        val response = erbsService?.getUserGamesResponse(userNumber, nextPage)
        val userGames = response?.result ?: listOf()
        addAdditionalParamUserGames(userGames, mmr)

        addCharacterWebLink(userGames)
        addItemWeaponTypeWebLink(userGames)
        addItemWebLink(userGames)

        return response
    }


    private fun addCharacterWebLink(userGames: List<UserGame>) {

        val characters = getCharacters()

        userGames.forEach { userGame ->
            userGame.characterImageWebLink =
                characters.first { userGame.characterNum == it.code }.iconWebLink
        }
    }

    private suspend fun addItemWeaponTypeWebLink(userGames: List<UserGame>) {
        val itemsWeapon = erbsService?.getItemsWeapon()?.result
        val itemWeaponImage = imageService.getWeaponTypeFiles()

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
                    userGame.weaponTypeImageWebLink = itemWeaponImage.first { driveFile ->
                        imageService.compareNames(driveFile.name, findType)
                    }.getWebLink(roomService)
                } catch (e: Exception) {
                    Timber.e(e)
                }

            }
            equipmentList.clear()
        }
    }

    private suspend fun addItemWebLink(userGames: List<UserGame>) {

        val equipmentLinks = mutableMapOf<Int, String?>()

        userGames.forEach { userGame ->
            equipmentLinks += userGame.equipment.item1Id to null
            equipmentLinks += userGame.equipment.item2Id to null
            equipmentLinks += userGame.equipment.item3Id to null
            equipmentLinks += userGame.equipment.item4Id to null
            equipmentLinks += userGame.equipment.item5Id to null
            equipmentLinks += userGame.equipment.item6Id to null
        }

        val itemImage = imageService.getItemImage()

        itemImage.let {
            fillItemsImage(equipmentLinks, it, erbsService?.getItemsWeapon()?.result)
            fillItemsImage(equipmentLinks, it, erbsService?.getItemsArmor()?.result)
            fillItemsImage(equipmentLinks, it, erbsService?.getItemsSpecial()?.result)
        }

        userGames.forEach { userGame ->
            userGame.equipment.item1WebLink = equipmentLinks[userGame.equipment.item1Id] ?: ""
            userGame.equipment.item2WebLink = equipmentLinks[userGame.equipment.item2Id] ?: ""
            userGame.equipment.item3WebLink = equipmentLinks[userGame.equipment.item3Id] ?: ""
            userGame.equipment.item4WebLink = equipmentLinks[userGame.equipment.item4Id] ?: ""
            userGame.equipment.item5WebLink = equipmentLinks[userGame.equipment.item5Id] ?: ""
            userGame.equipment.item6WebLink = equipmentLinks[userGame.equipment.item6Id] ?: ""

        }

    }

    suspend private fun fillItemsImage(
        equipmentLinks: MutableMap<Int, String?>,
        itemsImages: List<FoundItem>,
        items: List<SearchItem>?
    ) {

        val foundItems = mutableListOf<SearchItem?>()

        equipmentLinks.forEach { equipmentLink ->
            items.let { thisItem ->
                foundItems.add(thisItem?.find {
                    it.code == equipmentLink.key
                })
            }
        }


        foundItems.filterNotNull().forEach { foundItem ->

            val foundItemName = foundItem.name ?: ""
            val foundItemCode = foundItem.code ?: 0

            if (foundItemName != "" && foundItemCode != 0) {
                imageService.let { thisImageService ->
                    val findDriveFile = itemsImages.find { foundImage ->
                        thisImageService.compareNames(foundImage.name, foundItemName)
                    }
                    equipmentLinks += foundItemCode to findDriveFile?.getWebLink(roomService)
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
            imageService.compareNames(character.name, name) ?: (character.name == name)
        }
    }

    suspend fun getCharacterWeaponTypes(code: Int): List<WeaponType> {
        val weaponTypes =
            erbsService?.getCharacterWeaponTypes()?.result?.filter { it.characterCode == code }
        val weaponTypeFiles = imageService.getWeaponTypeFiles()

        weaponTypes?.forEach { weaponType ->
            weaponType.mastery?.let { mastery ->
                try {
                    weaponType.weaponTypeImageWebLink = weaponTypeFiles.first { driveFile ->
                        imageService.compareNames(driveFile.name, mastery) ?: false
                    }?.getWebLink(roomService)
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
    private suspend fun addRankTierImage(ranks: List<Rank>) {

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

        val files = imageService.getRankTierFile(*mmrSet.toIntArray())

        searchTierRankImageMap.forEach { mapEntry ->
            searchTierRankImageMap[mapEntry.key] = files?.find { driveFile ->
                imageService.compareNames(driveFile.name, mapEntry.key) ?: false
            }?.getWebLink(roomService)

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

    suspend fun addCharacterHalfWebLink(characterStats: CharacterStats) {
        characterStats.name
        imageService.let { thisImageService ->
            characterStats.characterImageHalfWebLink = thisImageService.getCharacterImageFiles(
                CharacterImageType.HALF,
                characterStats.name
            )?.getWebLink(roomService)
        }

    }

    fun getCoreCharacter(code: Int) = roomService?.let { thisRoomService ->
        thisRoomService.getCoreCharacters()?.map { Converter.conv(it) }?.find { it.code == code }
    }

    suspend fun getSkills(code: Int): List<CoreSkill>? {
        val character = getCoreCharacter(code)

        character?.skills?.let { coreSkills ->
            character.name?.let { addSkillsImageLink(coreSkills, it) }
            addSkillsVideoLink(coreSkills)
        }

        return character?.skills
    }

    private suspend fun addSkillsImageLink(coreSkills: List<CoreSkill>, nameCharacter: String) {

        imageService?.let { thisImageService ->
            val imageWebLinks = thisImageService.getSkillImage(nameCharacter)

            coreSkills.forEach { coreSkill ->
                coreSkill.key?.let { key ->
                    coreSkill.image = imageWebLinks.find { driveFile ->
                        thisImageService.compareNames(
                            driveFile.name,
                            key
                        )
                    }?.getWebLink(roomService)
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

        val foundUsers = mutableListOf<User>()

        try {
            erbsService?.getUserResponse(searchString)?.result?.let { foundUsers.add(it) }
        } catch (e: Exception) {

        }

        return foundUsers

    }

    /////

    private suspend fun <T : Any> addIconWebLink(
        items: List<T>,
        namePropertyImageLink: String,
        namePropertyName: String
    ) {
        imageService.let { thisImageService ->
            val miniImageItems = thisImageService.getCharacterImageMiniLink()
            items.forEach { item ->

                val foundName = getInstanceProperty<String?>(item, namePropertyName)

                foundName?.let { thisFoundName ->
                    val link = miniImageItems.find {
                        thisImageService.compareNames(
                            it.name,
                            thisFoundName
                        )
                    }?.getWebLink(roomService)
                    setInstanceProperty(item, namePropertyImageLink, link)

                }

            }
        }
    }

    private fun <R> getInstanceProperty(instance: Any, propertyName: String): R {
        val property = instance::class.members.first { it.name == propertyName } as KProperty<*>
        return property.getter.call(instance)  as R
    }

    private fun <R> setInstanceProperty(instance: Any, propertyName: String, value: R) {
        val property = instance::class.members.first { it.name == propertyName }
        if (property is KMutableProperty<*>) {
            property.setter.call(instance, value)
        }
    }

}