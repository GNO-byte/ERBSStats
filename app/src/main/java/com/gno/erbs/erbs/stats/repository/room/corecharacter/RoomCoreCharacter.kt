package com.gno.erbs.erbs.stats.repository.room.corecharacter

import androidx.room.*

@Entity
//(indices = [Index(value = ["code"], unique = true)])
data class RoomCoreCharacter(
    @PrimaryKey
    @ColumnInfo(index = true)
    val code: Int,
    val background: String?,
    val name: String?
) {
    @Ignore
    var skills: List<RoomCoreSkill>? = null
    @Ignore
    var weapons: List<RoomCoreWeapon>? = null
    @Ignore
    constructor(
        code: Int,
        background: String?,
        name: String?,
        skills: List<RoomCoreSkill>?,
        weapons: List<RoomCoreWeapon>?
    ) : this(code, background, name) {
        this.skills = skills
        this.weapons = weapons
    }
}