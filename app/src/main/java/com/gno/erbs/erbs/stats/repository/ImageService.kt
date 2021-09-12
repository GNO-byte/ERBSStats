package com.gno.erbs.erbs.stats.repository

import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.repository.drive.CharacterImageType

interface ImageService {

    var coreCharacters: List<CoreCharacter>?

    suspend fun getCharacterImageMiniLink(): List<FoundItem>

    suspend fun getCharacterImageFiles(
        characterImageType: CharacterImageType,
        characterName: String
    ): FoundItem?

    suspend fun getRankTierFile(vararg mmrs: Int): List<FoundItem>

    suspend fun getSkillImage(characterName: String): List<FoundItem>

    suspend fun getWeaponTypeFiles()
            : List<FoundItem>

    suspend fun getItemImage(): List<FoundItem>

    suspend fun getDataVersion(): Int?

    suspend fun getIllustrations(): List<FoundItem>

    fun compareNames(nameFile: String, searchName: String): Boolean

}