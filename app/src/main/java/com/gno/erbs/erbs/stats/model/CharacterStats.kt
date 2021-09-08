package com.gno.erbs.erbs.stats.model

import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.model.erbs.characters.CharacterLevelUpStat
import java.text.DecimalFormat

class CharacterStats private constructor(
    var code: Int,
    var name: String,
    var attackPower: DoubleParameter<Double>,
    var attackSpeed: DoubleParameter<Double>,
    var criticalChance: DoubleParameter<Double>,
    var defence: DoubleParameter<Double>,
    var hpRegen: DoubleParameter<Double>,
    var maxHp: DoubleParameter<Double>,
    var maxSp: DoubleParameter<Double>,
    var moveSpeed: DoubleParameter<Double>,
    var spRegen: DoubleParameter<Double>,

    val attackSpeedLimit: Double,
    val attackSpeedMin: Double,
    val radius: Double,
    val sightRange: Int,
    var characterImageHalfWebLink: String? = null

) {
    companion object {

        private const val MAX_LVL = 20.0
        operator fun invoke(
            character: Character,
            characterLevelUp: CharacterLevelUpStat
        ): CharacterStats {

            return CharacterStats(
                character.code,
                character.name,
                DoubleParameter(
                    "Attack power",
                    character.attackPower,
                    calculateValueMaxLvl(character.attackPower, characterLevelUp.attackPower ?: 0.0)
                ),
                DoubleParameter(
                    "Attack speed",
                    character.attackSpeed,
                    calculateValueMaxLvl(character.attackSpeed, characterLevelUp.attackSpeed ?: 0.0)
                ),
                DoubleParameter(
                    "Critical chance",
                    character.criticalStrikeChance,
                    calculateValueMaxLvl(
                        character.criticalStrikeChance,
                        characterLevelUp.criticalChance ?: 0.0
                    )
                ),
                DoubleParameter(
                    "Defence",
                    character.defense,
                    calculateValueMaxLvl(character.defense, characterLevelUp.defense ?: 0.0)
                ),
                DoubleParameter(
                    "HP regen",
                    character.hpRegen,
                    calculateValueMaxLvl(character.hpRegen, characterLevelUp.hpRegen ?: 0.0)
                ),
                DoubleParameter(
                    "HP",
                    character.maxHp,
                    calculateValueMaxLvl(character.maxHp, characterLevelUp.maxHp ?: 0.0)
                ),
                DoubleParameter(
                    "SP",
                    character.maxSp,
                    calculateValueMaxLvl(character.maxSp, characterLevelUp.maxSp ?: 0.0)
                ),
                DoubleParameter(
                    "Move speed",
                    character.moveSpeed,
                    calculateValueMaxLvl(character.moveSpeed, characterLevelUp.moveSpeed ?: 0.0)
                ),
                DoubleParameter(
                    "SP",
                    character.spRegen,
                    calculateValueMaxLvl(character.spRegen, characterLevelUp.spRegen ?: 0.0)
                ),
                character.attackSpeedLimit,
                character.attackSpeedMin,
                character.radius,
                character.sightRange
            )
        }

        private fun calculateValueMaxLvl(minLVLValue: Double, LevelUpValue: Double): Double {
            return minLVLValue + (MAX_LVL - 1) * LevelUpValue
        }
    }
}