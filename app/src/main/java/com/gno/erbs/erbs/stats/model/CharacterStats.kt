package com.gno.erbs.erbs.stats.model

import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.model.erbs.characters.CharacterLevelUpStat

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
        ): CharacterStats? {


            return if (character.code == null || character.name == null) null else CharacterStats(
                character.code,
                character.name,
                DoubleParameter(
                    "Attack power",
                    character.attackPower ?: 0.0,
                    calculateValueMaxLvl(
                        character.attackPower ?: 0.0,
                        characterLevelUp.attackPower ?: 0.0
                    )
                ),
                DoubleParameter(
                    "Attack speed",
                    character.attackSpeed ?: 0.0,
                    calculateValueMaxLvl(
                        character.attackSpeed ?: 0.0,
                        characterLevelUp.attackSpeed ?: 0.0
                    )
                ),
                DoubleParameter(
                    "Critical chance",
                    character.criticalStrikeChance ?: 0.0,
                    calculateValueMaxLvl(
                        character.criticalStrikeChance ?: 0.0,
                        characterLevelUp.criticalChance ?: 0.0
                    )
                ),
                DoubleParameter(
                    "Defence",
                    character.defense ?: 0.0,
                    calculateValueMaxLvl(character.defense ?: 0.0, characterLevelUp.defense ?: 0.0)
                ),
                DoubleParameter(
                    "HP regen",
                    character.hpRegen ?: 0.0,
                    calculateValueMaxLvl(character.hpRegen ?: 0.0, characterLevelUp.hpRegen ?: 0.0)
                ),
                DoubleParameter(
                    "HP",
                    character.maxHp ?: 0.0,
                    calculateValueMaxLvl(character.maxHp ?: 0.0, characterLevelUp.maxHp ?: 0.0)
                ),
                DoubleParameter(
                    "SP",
                    character.maxSp ?: 0.0,
                    calculateValueMaxLvl(character.maxSp ?: 0.0, characterLevelUp.maxSp ?: 0.0)
                ),
                DoubleParameter(
                    "Move speed",
                    character.moveSpeed ?: 0.0,
                    calculateValueMaxLvl(
                        character.moveSpeed ?: 0.0,
                        characterLevelUp.moveSpeed ?: 0.0
                    )
                ),
                DoubleParameter(
                    "SP",
                    character.spRegen ?: 0.0,
                    calculateValueMaxLvl(character.spRegen ?: 0.0, characterLevelUp.spRegen ?: 0.0)
                ),
                character.attackSpeedLimit ?: 0.0,
                character.attackSpeedMin ?: 0.0,
                character.radius ?: 0.0,
                character.sightRange ?: 0
            )
        }

        private fun calculateValueMaxLvl(minLVLValue: Double, LevelUpValue: Double): Double {
            return ((minLVLValue + (MAX_LVL - 1) * LevelUpValue) * 100.0).toInt() / 100.0
        }
    }
}