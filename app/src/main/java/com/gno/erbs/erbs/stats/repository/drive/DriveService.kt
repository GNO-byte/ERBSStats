package com.gno.erbs.erbs.stats.repository.drive

import android.content.Context
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.model.drive.imagelinkstructure.ImagesLinkStructure
import com.gno.erbs.erbs.stats.repository.KeysHelper
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

object DriveService {

    private var coreImageLinkStructureId: String? = null
    private var coreCharactersId: String? = null

    private lateinit var service: Drive
    var imagesLinkStructure: ImagesLinkStructure? = null
    var coreCharacters: List<CoreCharacter>? = null

    operator fun invoke(context: Context): DriveService {

        coreImageLinkStructureId = KeysHelper.getDriveCoreImageLinkStructureId(context)
        coreCharactersId = KeysHelper.getDriveCoreCharactersId(context)

        val credential =
            GoogleCredential.fromStream(KeysHelper.getDriveKey(context).byteInputStream())
                .createScoped(listOf("https://www.googleapis.com/auth/drive"))

        service =
            Drive.Builder(ApacheHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Drive API Migration")
                .build()

        return this

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

    fun getCharacterImageFiles(
        characterImageType: CharacterImageType,
        vararg characterNames: String
    ): List<File> {

        val driveIdFolder = when (characterImageType) {
            CharacterImageType.MINI -> imagesLinkStructure?.characterMini
            CharacterImageType.HALF -> imagesLinkStructure?.characterHalf
            CharacterImageType.FULL -> imagesLinkStructure?.characterFull

        }

        val files = service.files().list()
            .setQ("trashed = false and '$driveIdFolder' in parents")
            .setFields("files(id, name,webContentLink)")
            .execute().files

        val resultFiles = mutableListOf<File>()
        for (characterName in characterNames) {
            val foundFile = files.find{ compareNames(it.name, characterName)}
            foundFile?.let{
                resultFiles.add(it)
            }
        }


        return resultFiles
    }

    fun getFolderFiles(catalogId: String): List<File>? {
        return service.files().list()
            .setQ(" trashed = false and '$catalogId' in parents")
            .setFields("files(id, name,webContentLink)")
            .execute().files
    }

    fun getRankTierFile(vararg mmrs: Int): List<File> {

        val files = imagesLinkStructure?.rankTier?.let { getFolderFiles(it) }

        val resultFiles = mutableListOf<File>()
        files?.let { thisFiles ->

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

                thisFiles.find { compareNames(it.name, searchName) }?.let { resultFiles.add(it) }
            }
        }

        return resultFiles
    }

    fun getSkillImage(characterName: String): List<File>? {
        val driveFiles = imagesLinkStructure?.skillIconCharacterFolders?.let { getFolderFiles(it) }
        val characterSkillsFolder = driveFiles?.find { compareNames(it.name, characterName) }

        return characterSkillsFolder?.let { getFolderFiles(it.id) }
    }

    fun compareNames(nameFile: String, searchName: String): Boolean {
        return nameFile.contains(searchName, true) ||
                nameFile.replace(" ", "").contains(
                    searchName.replace(" ", ""), true
                )
    }

    fun getWeaponTypeFiles()
            : List<File>? {
        imagesLinkStructure?.skillIconWeapon?.let {
            return getFolderFiles(it)
        }
        return null
    }

    fun getCharacterHalfFiles(): List<File>? {
        imagesLinkStructure?.characterHalf?.let {
            return getFolderFiles(it)
        }
        return null
    }

}