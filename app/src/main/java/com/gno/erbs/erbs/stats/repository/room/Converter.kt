package com.gno.erbs.erbs.stats.repository.room

import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreCharacter
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreWeapon
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.repository.room.character.RoomCharacter
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreCharacter
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreSkill
import com.gno.erbs.erbs.stats.repository.room.corecharacter.RoomCoreWeapon

object Converter {

    fun conv(character: Character) = RoomCharacter(
        null,
        null,
        character.attackPower,
        character.attackSpeed,
        character.attackSpeedLimit,
        character.attackSpeedMin,
        character.code,
        character.criticalStrikeChance,
        character.defense,
        character.hpRegen,
        character.initExtraPoint,
        character.maxExtraPoint,
        character.maxHp,
        character.maxSp,
        character.moveSpeed,
        character.name,
        character.radius,
        character.resource,
        character.sightRange,
        character.spRegen,
        character.uiHeight,
        character.iconWebLink
    )

    fun conv(character: RoomCharacter) = Character(
        character.attackPower,
        character.attackSpeed,
        character.attackSpeedLimit,
        character.attackSpeedMin,
        character.code,
        character.criticalStrikeChance,
        character.defense,
        character.hpRegen,
        character.initExtraPoint,
        character.maxExtraPoint,
        character.maxHp,
        character.maxSp,
        character.moveSpeed,
        character.name,
        character.radius,
        character.resource,
        character.sightRange,
        character.spRegen,
        character.uiHeight,
        character.iconWebLink
    )

    fun conv(coreCharacter: CoreCharacter) = coreCharacter.code?.let { code ->
        RoomCoreCharacter(code,
            coreCharacter.background,
            coreCharacter.name,
            coreCharacter.skills?.mapNotNull { conv(it, code) },
            coreCharacter.weapons?.mapNotNull { conv(it, code) }
        )
    }

    fun conv(coreSkill: CoreSkill, characterCode: Int) = coreSkill.id?.let { id ->
        RoomCoreSkill(
            id,
            characterCode,
            coreSkill.description,
            coreSkill.group,
            coreSkill.key,
            coreSkill.name,
            coreSkill.type,
            coreSkill.image,
            coreSkill.videoLink
        )
    }

    fun conv(coreWeapon: CoreWeapon, characterCode: Int) = coreWeapon.id?.let { id ->
        RoomCoreWeapon(id, characterCode, coreWeapon.name)
    }

    fun conv(coreCharacter: RoomCoreCharacter) =
        CoreCharacter(coreCharacter.code,
            coreCharacter.background,
            coreCharacter.name,
            coreCharacter.skills?.map { conv(it) },
            coreCharacter.weapons?.map { conv(it) }
        )

    fun conv(coreSkill: RoomCoreSkill) = CoreSkill(
        coreSkill.id,
        coreSkill.description,
        coreSkill.group,
        coreSkill.key,
        coreSkill.name,
        coreSkill.type,
        coreSkill.image,
        coreSkill.videoLink
    )

    fun conv(coreWeapon: RoomCoreWeapon) = CoreWeapon(coreWeapon.id, coreWeapon.name)
}