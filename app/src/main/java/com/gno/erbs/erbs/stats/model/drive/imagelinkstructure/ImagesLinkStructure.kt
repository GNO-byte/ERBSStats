package com.gno.erbs.erbs.stats.model.drive.imagelinkstructure

data class ImagesLinkStructure(
    val characterFull: String?,
    val characterHalf: String?,
    val characterMini: String?,
    val rankTier: String?,
    val skillIconCharacterFolders: String?,
    val skillIconWeapon: String?,
    val weapon: List<Weapon>?,
    val armor: List<Armor>?,
    val consumable: List<Consumable>?,
    val special: List<Special>?,
    val ingredients: List<Ingredient>?
)