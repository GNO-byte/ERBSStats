package com.gno.erbs.erbs.stats.repository.drive

import android.content.Context
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.model.drive.imagelinkstructure.ImagesLinkStructure
import com.gno.erbs.erbs.stats.repository.FoundItem
import com.gno.erbs.erbs.stats.repository.ImageService
import com.gno.erbs.erbs.stats.repository.KeysHelper
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

object DriveService : ImageService {

    private var coreImageLinkStructureId: String? = null
    private var coreCharactersId: String? = null

    private lateinit var service: Drive
    var imagesLinkStructure: ImagesLinkStructure? = null
    override var coreCharacters: List<CoreCharacter>? = null

    operator fun invoke(context: Context): DriveService {

        coreImageLinkStructureId = KeysHelper.getDriveCoreImageLinkStructureId(context)
        coreCharactersId = KeysHelper.getDriveCoreCharactersId(context)

        val credential =
            GoogleCredential.fromStream(KeysHelper.getDriveKey(context).byteInputStream())
                .createScoped(listOf("https://www.googleapis.com/auth/drive"))

        service =
            Drive.Builder(ApacheHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("ERBS")
                .build()

        GlobalScope.launch {
            initImagesLinkStructure()
            initCoreCharacters()
        }

        return this

    }

    private fun readString(inputStream: InputStream): String {
        val buf = CharArray(2048)
        val r: Reader = InputStreamReader(inputStream, "UTF-8")
        val s = StringBuilder()
        while (true) {
            val n: Int = r.read(buf)
            if (n < 0) break
            s.append(buf, 0, n)
        }
        return s.toString()
    }

    fun initImagesLinkStructure() {

        val inputStream = service.files().get(coreImageLinkStructureId)
            .executeMediaAsInputStream()

        val stringJson = readString(inputStream)

        imagesLinkStructure = Gson().fromJson(stringJson, ImagesLinkStructure::class.java)

    }

    fun initCoreCharacters() {

        val inputStream = service.files().get(coreCharactersId)
            .executeMediaAsInputStream()

        val stringJson = readString(inputStream)

        val turnsType = object : TypeToken<List<CoreCharacter>>() {}.type
        coreCharacters = Gson().fromJson(stringJson, turnsType)


    }

    ///////

    override suspend fun getCharacterImageMiniLink(): List<FoundItem> {

        val imageMiniFiles = imagesLinkStructure?.charactersImagesMini?.let { getFolderFiles(it) }
        return if (imageMiniFiles != null) createFoundItems(imageMiniFiles) else listOf()

    }

    override suspend fun getCharacterImageFiles(
        characterImageType: CharacterImageType,
        characterName: String
    ): FoundItem? {

        val characterImagesFolder = getFilesCharacterSubFolder(characterName, "Default")
        val foundCharacterImage =
            characterImagesFolder?.find { compareNames(it.name, characterImageType.name) }

        return if (foundCharacterImage != null) FoundItem(
            foundCharacterImage.name,
            foundCharacterImage.webContentLink
        ) else null

    }

    override suspend fun getRankTierFile(vararg mmrs: Int): List<FoundItem> {

        val files = imagesLinkStructure?.rankTier?.let { getFolderFiles(it) }

        val resultFiles = mutableListOf<File>()
        files?.let { files ->

            for (mmr in mmrs) {

                val searchName = when {
                    mmr < 400 -> "Iron"
                    mmr < 800 -> "Bronze"
                    mmr < 1200 -> "Silver"
                    mmr < 1600 -> "Gold"
                    mmr < 2000 -> "Platinum"
                    mmr < 2400 -> "Diamon"
                    mmr < 2800 -> "Titan"
                    mmr < 2800 -> "Titan"
                    mmr >= 2800 -> "Immortal"
                    else -> "Unrank"
                }

                files.find { compareNames(it.name, searchName) }?.let { resultFiles.add(it) }
            }
        }

        return createFoundItems(resultFiles)
    }

    override suspend fun getSkillImage(characterName: String): List<FoundItem> {

        val skillsImagesFolder = getFilesCharacterSubFolder(characterName, "Skill icon")
        return if (skillsImagesFolder != null) createFoundItems(skillsImagesFolder) else listOf()

    }

    override suspend fun getWeaponTypeFiles()
            : List<FoundItem> {

        val skillIconWeaponFiles = imagesLinkStructure?.skillIconWeapon?.let { getFolderFiles(it) }
        return if (skillIconWeaponFiles != null) createFoundItems(skillIconWeaponFiles) else listOf()

    }

    override suspend fun getItemImage(): List<FoundItem> {
        val itemImageFiles = imagesLinkStructure?.equipment?.let { getFolderFiles(it) }
        return if (itemImageFiles != null) createFoundItems(itemImageFiles) else listOf()
    }

    override suspend fun getDataVersion() = 0

    ////////

    private fun getFilesCharacterSubFolder(characterName: String, nameFolder: String): List<File>? {

        var foundFiles: List<File>? = null

        imagesLinkStructure?.let { imagesLinkStructure ->

            val charactersFolders =
                imagesLinkStructure.charactersFolder?.let { getFolderFiles(it) }

            val foundCharacterFolder =
                charactersFolders?.find { compareNames(it.name, characterName) }

            val characterFolder = foundCharacterFolder?.id?.let { getFolderFiles(it) }

            val foundImageCharacterFolder =
                characterFolder?.find { compareNames(it.name, nameFolder) }

            foundFiles = foundImageCharacterFolder?.let { getFolderFiles(it.id) }

        }
        return foundFiles
    }

    private fun getFolderFiles(catalogId: String): List<File> {

        val foundFile = mutableListOf<File>()


        var response = service.files().list()
            .setQ(" trashed = false and '$catalogId' in parents")
            .setPageSize(500)
            .setFields("nextPageToken,files(id, name,webContentLink)")
            .execute()

        foundFile.addAll(response.files)

        var token = response.nextPageToken

        while (token != null) {

            response = service.files().list()
                .setQ(" trashed = false and '$catalogId' in parents")
                .setPageSize(1000)
                .setPageToken(token)
                .setFields("nextPageToken,files(id, name,webContentLink)")
                .execute()

            token = response.nextPageToken
            foundFile.addAll(response.files)

        }

        return foundFile
    }


    private fun createFoundItems(driveFiles: List<File>): List<FoundItem> = driveFiles.map {
        FoundItem(it.name, it.webContentLink)
    }

    override fun compareNames(nameFile: String, searchName: String): Boolean {
        return nameFile.contains(searchName, true) ||
                nameFile.replace(" ", "").contains(
                    searchName.replace(" ", ""), true
                )
    }

    override suspend fun getIllustrations(): List<FoundItem> {
        return emptyList()
    }


}