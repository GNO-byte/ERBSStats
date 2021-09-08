package com.gno.erbs.erbs.stats.repository

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.gno.erbs.erbs.stats.model.CharacterStats
import com.gno.erbs.erbs.stats.model.TeamMode
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.model.erbs.Response
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

object DataRepository {

    private val aesopService = AesopService()
    private var erbsService: ERBSService? = null
    private var imageService: ImageService? = null
    private val defaultValue = DefaultValue
    private var roomService: RoomService? = null

    val navigationHistoryLiveData by lazy {
        roomService?.getLast5Histories()?.map { roomHistories ->
            roomHistories?.map { Converter.conv(it) }
        }?.asLiveData()
    }

    operator fun invoke(context: Context): DataRepository {
        erbsService = ERBSService(context)
        roomService = RoomService(context)
        imageService = FirebaseService(context)
        return this
    }

    suspend fun initData(context: Context) {

        val dataVersion = DefaultValue.getDataVersion(context)
        val exchangeDataVersion = imageService?.getDataVersion()
        if (dataVersion != exchangeDataVersion) {
            updateCharacters()
            updateCoreCharacters()
            DefaultValue.setDataVersion(context, exchangeDataVersion ?: 0)
        }
    }

//    fun getNavigationHistoryLiveData(lifecycleOwner: LifecycleOwner): MutableLiveData<List<NavigationHistory>?> {
//
//        GlobalScope.launch(Dispatchers.Main) {
//
//            roomService?.getLast5Histories()?.asLiveData()
//
//            roomService?.getLast5Histories()?.observe(lifecycleOwner) { roomHistories ->
//                roomHistories?.let {
//                    navigationHistoryLiveData.postValue(roomHistories.map { Converter.conv(it) })
//                }
//            }
//        }
//
//        return navigationHistoryLiveData
//    }

    private suspend fun updateCharacters() {

        erbsService?.let { erbsService ->
            executeRequest { erbsService.getCharacters() }?.result?.also { characters ->
                addIconWebLink(characters, "iconWebLink", "name")
                roomService?.addCharacters(characters.map { Converter.conv(it) })
            }
        }
    }

    private fun updateCoreCharacters() {

        roomService?.let { roomService ->
            imageService?.coreCharacters?.mapNotNull { Converter.conv(it) }?.let {
                roomService.addCoreCharacters(it)
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

    fun getCharacters() = roomService?.getCharacters()?.map { Converter.conv(it) }

    suspend fun getUserStats(
        userNumber: String,
        seasonId: String,
        characters: List<Character>?
    ) = erbsService?.let { erbsService ->
        executeRequest {
            erbsService.getUserStatsResponse(
                userNumber,
                seasonId
            )
        }?.result?.also {
            addAdditionalParamCharacterStat(it, characters)
        }
    }


    private suspend fun addAdditionalParamCharacterStat(
        userStats: List<UserStats>?,
        characters: List<Character>?
    ) {

        if (userStats != null && characters != null) {
            userStats.forEach { userStatsList ->
                userStatsList.characterStats.forEach { characterStats ->
                    try {
                        characterStats.characterName =
                            characters.find { it.code == characterStats.characterCode }?.name
                        characterStats.matchingTeamMode = userStatsList.matchingTeamMode
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }

            addRankTierImage(userStats)
            addTopCharacterImage(userStats)
        }

    }

    private suspend fun addTopCharacterImage(userStats: List<UserStats>) {

        imageService.let { imageService ->
            userStats.forEach { userStat ->
                val topCharacterName =
                    userStat.characterStats.maxByOrNull {
                        it.usages ?: 0
                    }?.characterName

                topCharacterName?.let { characterName ->
                    userStat.topCharacterHalfImageWebLink =
                        DataRepository.imageService?.getCharacterImageFiles(
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

        val files = imageService?.getRankTierFile(*mmrSet.toIntArray())

        searchTierRankImageMap.forEach { mapEntry ->
            searchTierRankImageMap[mapEntry.key] = files?.find { driveFile ->
                imageService?.compareNames(driveFile.name, mapEntry.key) ?: false
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
        nextPage: String
    ) = erbsService?.let { erbsService ->
        executeRequest {
            erbsService.getUserGamesResponse(
                userNumber,
                nextPage
            )
        }?.also { response ->

            response.result?.also { userGames ->
                addAdditionalParamUserGames(userGames)
                addCharacterWebLink(userGames)
                addItemWeaponTypeWebLink(userGames)
                addItemWebLink(userGames)
            }
        }
    }


    private fun addCharacterWebLink(userGames: List<UserGame>) {
        val characters = getCharacters()
        userGames.forEach { userGame ->
            userGame.characterImageWebLink =
                characters?.find { userGame.characterNum == it.code }?.iconWebLink
        }
    }

    private suspend fun addItemWeaponTypeWebLink(userGames: List<UserGame>) {
        val itemsWeapon =
            erbsService?.let { erbsService ->
                executeRequest { erbsService.getItemsWeapon() }
            }?.result

        val itemWeaponImage = imageService?.getWeaponTypeFiles()

        val equipmentList = mutableListOf<Int>()

        userGames.forEach { userGame ->

            equipmentList += userGame.equipment.item1Id
            equipmentList += userGame.equipment.item2Id
            equipmentList += userGame.equipment.item3Id
            equipmentList += userGame.equipment.item4Id
            equipmentList += userGame.equipment.item5Id
            equipmentList += userGame.equipment.item6Id

            var findType: String? = null
            equipmentList.takeWhile { findType == null }.forEach {
                try {
                    findType = itemsWeapon?.find { itemWeapon ->
                        itemWeapon.code == it
                    }?.weaponType
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            userGame.weaponTypeImageWebLink = findType?.let { findType ->
                itemWeaponImage?.find { driveFile ->
                    imageService?.compareNames(driveFile.name, findType) ?: false
                }?.getWebLink(roomService)
            }

            equipmentList.clear()
        }
    }

    private suspend fun addItemWebLink(userGames: List<UserGame>) {

        val equipmentLinks = mutableMapOf<Int, String?>()

        userGames.forEach { userGame ->

            equipmentLinks += userGame.equipment.item1Id to emptyItemCheck(userGame.equipment.item1Id)
            equipmentLinks += userGame.equipment.item2Id to emptyItemCheck(userGame.equipment.item2Id)
            equipmentLinks += userGame.equipment.item3Id to emptyItemCheck(userGame.equipment.item3Id)
            equipmentLinks += userGame.equipment.item4Id to emptyItemCheck(userGame.equipment.item4Id)
            equipmentLinks += userGame.equipment.item5Id to emptyItemCheck(userGame.equipment.item5Id)
            equipmentLinks += userGame.equipment.item6Id to emptyItemCheck(userGame.equipment.item6Id)
        }

        val itemImage = imageService?.getItemImage()

        itemImage?.let {
            fillItemsImage(
                equipmentLinks, it, erbsService?.let { erbsService ->
                    executeRequest { erbsService.getItemsWeapon() }
                }?.result
            )
            fillItemsImage(
                equipmentLinks, it, erbsService?.let { erbsService ->
                    executeRequest { erbsService.getItemsArmor() }
                }?.result
            )
            fillItemsImage(
                equipmentLinks, it, erbsService?.let { erbsService ->
                    executeRequest { erbsService.getItemsSpecial() }
                }?.result
            )
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

    private fun emptyItemCheck(itemId: Int) = if (itemId == 0) "noItem" else null

    private suspend fun fillItemsImage(
        equipmentLinks: MutableMap<Int, String?>,
        itemsImages: List<FoundItem>,
        items: List<SearchItem>?
    ) {

        val foundItems = mutableListOf<SearchItem?>()

        equipmentLinks.forEach { equipmentLink ->
            items.let { items ->
                foundItems.add(items?.find {
                    it.code == equipmentLink.key
                })
            }
        }

        foundItems.filterNotNull().forEach { foundItem ->

            val foundItemName = foundItem.name ?: ""
            val foundItemCode = foundItem.code ?: 0

            if (foundItemName != "" && foundItemCode != 0) {
                imageService.let { imageService ->
                    val findDriveFile = itemsImages.find { foundImage ->
                        imageService?.compareNames(foundImage.name, foundItemName) ?: false
                    }
                    equipmentLinks += foundItemCode to findDriveFile?.getWebLink(roomService)
                }
            }
        }
    }


    private fun addAdditionalParamUserGames(userGames: List<UserGame>) {

        userGames.forEach { userGame ->
//            if (index != 0) userGames[index].mmr = userGames[index - 1].mmrBefore
//            else userGames[index].mmr = try {
//                lastMmr.getValue(userGames[index].matchingTeamMode)
//            } catch (e: Exception) {
//                Timber.e(e)
//                0
//            }
            userGame.teamMode =
                TeamMode.findByValue(userGame.matchingTeamMode)
            userGame.date = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                Locale.getDefault()
            ).parse(userGame.startDtm) ?: Date()

            userGame.equipment.item1WebLink = ""
            userGame.equipment.item2WebLink = ""
            userGame.equipment.item3WebLink = ""
            userGame.equipment.item4WebLink = ""
            userGame.equipment.item5WebLink = ""
            userGame.equipment.item6WebLink = ""
        }

    }

    fun getCharacter(code: Int) = getCharacters()?.find { it.code == code }

    fun getCharacter(name: String) = getCharacters()?.filter { character ->
        imageService?.compareNames(character.name, name) ?: false
    }

    suspend fun getCharacterWeaponTypes(code: Int): List<WeaponType>? {

        val weaponTypes =
            erbsService?.let { erbsService ->
                executeRequest {
                    erbsService.getCharacterWeaponTypes()
                }?.result?.filter { it.characterCode == code }
            }

        val weaponTypeFiles = imageService?.getWeaponTypeFiles()

        weaponTypes?.forEach { weaponType ->
            weaponType.mastery?.let { mastery ->
                try {
                    weaponType.weaponTypeImageWebLink = weaponTypeFiles?.first { driveFile ->
                        imageService?.compareNames(driveFile.name, mastery) ?: false
                    }?.getWebLink(roomService)
                } catch (e: java.lang.Exception) {
                    Timber.e(e)
                }
            }
        }

        return weaponTypes
    }

    suspend fun getCharacterLevelUpStat(code: Int) =
        erbsService?.let { erbsService ->
            executeRequest { erbsService.getCharacterLevelUpStat() }?.result?.first { it.code == code }
        }

    suspend fun getTopRanks(seasonId: String, teamMode: String) =
        erbsService?.let { erbsService ->
            executeRequest {
                erbsService.getTopRanksResponse(seasonId, teamMode)
            }?.result?.toList()?.also { addRankTierImage(it) }
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

        val files = imageService?.getRankTierFile(*mmrSet.toIntArray())

        searchTierRankImageMap.forEach { mapEntry ->
            searchTierRankImageMap[mapEntry.key] = files?.find { driveFile ->
                imageService?.compareNames(driveFile.name, mapEntry.key) ?: false
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
        imageService.let { imageService ->
            characterStats.characterImageHalfWebLink = imageService?.getCharacterImageFiles(
                CharacterImageType.HALF,
                characterStats.name
            )?.getWebLink(roomService)
        }

    }

    fun getCoreCharacter(code: Int) = roomService?.let { roomService ->
        roomService.getCoreCharacters()?.map { Converter.conv(it) }?.find { it.code == code }
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

        imageService.let { imageService ->
            val imageWebLinks = imageService?.getSkillImage(nameCharacter)

            coreSkills.forEach { coreSkill ->
                coreSkill.key?.let { key ->
                    coreSkill.image = imageWebLinks?.find { driveFile ->
                        imageService.compareNames(
                            driveFile.name,
                            "_$key"
                        )
                    }?.getWebLink(roomService)
                }
            }
        }
    }

    private suspend fun addSkillsVideoLink(coreSkills: List<CoreSkill>) {

        erbsService?.let { erbsService ->
            val skillVideo = erbsService.getCharacterSkillVideos().result
            coreSkills.forEach { coreSkill ->
                coreSkill.videoLink = skillVideo?.find { it.code == coreSkill.group }?.youTubeUrl
            }
        }

    }

    suspend fun getUser(searchString: String) = erbsService?.let { erbsService ->

        executeRequest { erbsService.getUserResponse(searchString) }?.let {
            if (it.message == "Not Found") {
                User("Not Found", 0)
            } else it.result

        }

    }

    suspend fun getIllustrations() =
        imageService?.getIllustrations()?.mapNotNull { it.getWebLink() }

    private suspend fun <T : Any> addIconWebLink(
        items: List<T>,
        namePropertyImageLink: String,
        namePropertyName: String
    ) {
        imageService.let { imageService ->
            val miniImageItems = imageService?.getCharacterImageMiniLink()
            items.forEach { item ->

                val foundName = getInstanceProperty<String?>(item, namePropertyName)

                foundName?.let { foundName ->
                    val link = miniImageItems?.find {
                        imageService.compareNames(
                            it.name,
                            foundName
                        )
                    }?.getWebLink(roomService)
                    setInstanceProperty(item, namePropertyImageLink, link)

                }

            }
        }
    }


    fun addHistory(id: Int, bundle: Bundle?) {
        roomService?.addHistory(id, bundle)
    }

    private fun <R> getInstanceProperty(instance: Any, propertyName: String): R {
        val property = instance::class.members.first { it.name == propertyName } as KProperty<*>
        return property.getter.call(instance) as R
    }

    private fun <R> setInstanceProperty(instance: Any, propertyName: String, value: R) {
        val property = instance::class.members.first { it.name == propertyName }
        if (property is KMutableProperty<*>) {
            property.setter.call(instance, value)
        }
    }

    fun foundItemsWithoutPicture() {

        GlobalScope.launch {

            imageService?.getItemImage()?.let { itemImages ->

                foundCategoryItemWithoutPicture(
                    itemImages,
                    erbsService?.let { erbsService ->
                        executeRequest { erbsService.getItemsWeapon() }
                    }?.result,
                    "WEAPON"
                )
                foundCategoryItemWithoutPicture(
                    itemImages,
                    erbsService?.let { erbsService ->
                        executeRequest { erbsService.getItemsArmor() }
                    }?.result,
                    "ARMOR"
                )
                foundCategoryItemWithoutPicture(
                    itemImages,
                    erbsService?.let { erbsService ->
                        executeRequest { erbsService.getItemsSpecial() }
                    }?.result,
                    "SPECIAL"
                )
            }
        }

    }

    private fun foundCategoryItemWithoutPicture(
        itemsImages: List<FoundItem>, items: List<SearchItem>?, itemCategory: String
    ) {

        val noFoundName = sortedSetOf<String>()
        items?.forEach { it ->
            it.name?.let { itemName ->
                itemsImages.find { foundImage ->
                    imageService?.compareNames(foundImage.name, itemName) ?: false
                } ?: noFoundName.add(itemName)
            }
        }

        noFoundName.forEach {
            Timber.tag("$itemCategory NO FOUND").e(it)
        }


    }

    private inline fun <T> executeRequest(func: () -> Response<T>) = try {
        func.invoke()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }

}