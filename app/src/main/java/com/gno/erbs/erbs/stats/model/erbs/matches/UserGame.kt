package com.gno.erbs.erbs.stats.model.erbs.matches

import com.gno.erbs.erbs.stats.model.TeamMode
import java.util.*

data class UserGame(
    val accountLevel: Int?,
    val addSurveillanceCamera: Float?,
    val addTelephotoCamera: Float?,
    val amplifierToMonster: Float?,
    val attackPower: Float?,
    val attackRange: Double?,
    val attackSpeed: Double?,
    val bestWeapon: Float?,
    val bestWeaponLevel: Float?,
    val botAdded: Float?,
    val botRemain: Float?,
    val characterLevel: Float?,
    val characterNum: Int?,
    val coolDownReduction: Float?,
    val craftEpic: Float?,
    val craftLegend: Float?,
    val craftRare: Float?,
    val craftUncommon: Float?,
    val criticalStrikeChance: Float?,
    val criticalStrikeDamage: Float?,
    val damageFromMonster: Float?,
    val damageFromPlayer: Float?,
    val damageFromPlayer_basic: Float?,
    val damageFromPlayer_direct: Float?,
    val damageFromPlayer_itemSkill: Float?,
    val damageFromPlayer_skill: Float?,
    val damageFromPlayer_trap: Float?,
    val damageToMonster: Float?,
    val damageToMonster_basic: Float?,
    val damageToMonster_direct: Float?,
    val damageToMonster_itemSkill: Float?,
    val damageToMonster_skill: Float?,
    val damageToMonster_trap: Float?,
    val damageToPlayer: Float?,
    val damageToPlayer_basic: Float?,
    val damageToPlayer_direct: Float?,
    val damageToPlayer_itemSkill: Float?,
    val damageToPlayer_skill: Float?,
    val damageToPlayer_trap: Float?,
    val defense: Float?,
    val duration: Float?,
    val equipment: Equipment?,
    val gainExp: Float?,
    val gainedNormalMmrKFactor: Double?,
    val gameId: Int?,
    val gameRank: Int?,
    val giveUp: Float?,
    val healAmount: Float?,
    val hpRegen: Double?,
    val killMonsters: KillMonsters?,
    val killerUserNum: Float?,
    val killerUserNum2: Float?,
    val killerUserNum3: Float?,
    val language: String?,
    val lifeSteal: Double?,
    val masteryLevel: MasteryLevel?,
    val matchingMode: Float?,
    val matchingTeamMode: Int?,
    val maxHp: Float?,
    val maxSp: Float?,
    val mmrAvg: Int?,
    val mmrBefore: Int?,
    val monsterKill: Float?,
    val moveSpeed: Double?,
    val nickname: String?,
    val outOfCombatMoveSpeed: Double?,
    val placeOfStart: String?,
    val playTime: Float?,
    val playerAssistant: Float?,
    val playerKill: Float?,
    val preMade: Float?,
    val protectAbsorb: Float?,
    val removeSurveillanceCamera: Float?,
    val removeTelephotoCamera: Float?,
    val restrictedAreaAccelerated: Float?,
    val routeIdOfStart: Float?,
    val safeAreas: Float?,
    val seasonId: Float?,
    val serverName: String?,
    val sightRange: Double?,
    val skillLevelInfo: SkillLevelInfo?,
    val skillOrderInfo: SkillOrderInfo?,
    val skinCode: Float?,
    val spRegen: Double?,
    val startDtm: String?,
    val teamKill: Float?,
    val teamNumber: Float?,
    val teamRecover: Float?,
    val teamSpectator: Float?,
    val totalTime: Float?,
    val trapDamage: Float?,
    val useHyperLoop: Float?,
    val useSecurityConsole: Float?,
    val userNum: Float?,
    val versionMajor: Float?,
    val versionMinor: Float?,
    val victory: Float?,
    val watchTime: Float?,

    //additional param
    var mmr: Int?,
    var teamMode: TeamMode?,
    var date: Date?,
    var characterImageWebLink: String?,
    var weaponTypeImageWebLink: String?
)