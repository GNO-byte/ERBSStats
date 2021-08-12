package com.gno.erbs.erbs.stats.repository

import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.model.firebase.FolderContent
import com.gno.erbs.erbs.stats.repository.drive.CharacterImageType
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader


object FirebaseService : ImageService {

    var storageRef = FirebaseStorage.getInstance().reference
    override var coreCharacters: List<CoreCharacter>? = null

    ///////https://stackoverflow.com/questions/67918324/firebase-cloud-firestore-security-rules-only-allow-read-not-write

    init {


        GlobalScope.launch {
            initCoreCharacters()
        }
    }

    private suspend fun getFolderContent(folderPath: String): FolderContent {
        val listRef = storageRef.child(folderPath)
        return getFolderContent(listRef)

    }

    private suspend fun getFolderContent(storageReference: StorageReference): FolderContent {
        val folders = mutableListOf<StorageReference>()
        val files = mutableListOf<StorageReference>()

        storageReference.listAll()
            .addOnSuccessListener { (thisFiles, thisFolders) ->

                folders.addAll(thisFolders)
                files.addAll(thisFiles)

            }
            .addOnFailureListener {
                Timber.e(it)
            }.await()

        return FolderContent(folders, files)

    }

    suspend fun initCoreCharacters() {

        val file = storageRef.child("/ERBS/characters with weapons and skills.json")

        val stringJson = readString(file.stream.await().stream)

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


    override suspend fun getCharacterImageMiniLink(): List<FoundItem> {

        val imageMiniFolder = getFolderContent("/ERBS/Characters image mini")
        return createFoundItems(imageMiniFolder.files)

    }

    override suspend fun getCharacterImageFiles(
        characterImageType: CharacterImageType,
        characterName: String
    ): FoundItem? {

        val characterImages = getFilesContentCharacterSubFolder(characterName, "Default").files

        val foundCharacterImage = characterImages.find {
            compareNames(
                it.name,
                characterImageType.name
            )
        }

         val a = foundCharacterImage
        return if (foundCharacterImage != null) createFoundItem(foundCharacterImage) else null
    }


    override suspend fun getRankTierFile(vararg mmrs: Int): List<FoundItem> {

        val files = getFolderContent("/ERBS/Rank Tier").files


        val resultFiles = mutableListOf<StorageReference>()
        if (files.isNotEmpty()) {
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

                files.find {
                    compareNames(it.name, searchName)
                }?.let {
                    resultFiles.add(it)
                }
            }
        }

        return createFoundItems(resultFiles)

    }


    override suspend fun getSkillImage(characterName: String): List<FoundItem> {

        val skillsImagesFiles = getFilesContentCharacterSubFolder(characterName, "Skill Icon")
        return createFoundItems(skillsImagesFiles.files)

    }

    override suspend fun getWeaponTypeFiles(): List<FoundItem> {

        val imageMiniFolder = getFolderContent("/ERBS/weapon skills")
        return createFoundItems(imageMiniFolder.files)

    }

    override suspend fun getItemImage(): List<FoundItem> {

        val imageMiniFolder = getFolderContent("/ERBS/items icons")
        return createFoundItems(imageMiniFolder.files)
    }


    ////////////////////////


    private suspend fun getFilesContentCharacterSubFolder(
        characterName: String,
        nameFolder: String
    ): FolderContent {

        val charactersFolders = getFolderContent("/ERBS/Character").folders
        val characterFolder = charactersFolders.find { compareNames(it.name, characterName) }
        val foundFolder = characterFolder?.let{getFolderContent("${it.path}/$nameFolder")}
        return foundFolder ?: FolderContent()

    }


    override fun compareNames(nameFile: String, searchName: String): Boolean {
        return nameFile.contains(searchName, true) ||
                nameFile.replace(" ", "").contains(
                    searchName.replace(" ", ""), true
                )
    }

    private suspend fun createFoundItem(storageReference: StorageReference): FoundItem =
        FoundItem(storageReference.name, storageReference.downloadUrl.await().toString())

    private suspend fun createFoundItems(driveFiles: List<StorageReference>): List<FoundItem> =
        driveFiles.map {
            FoundItem(it.name, null,it)
        }
}