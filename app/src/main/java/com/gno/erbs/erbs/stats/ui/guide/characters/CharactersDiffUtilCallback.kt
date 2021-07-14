package com.gno.erbs.erbs.stats.ui.guide.characters

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.erbs.characters.Character

class CharactersDiffUtilCallback : DiffUtil.ItemCallback<Character>() {

    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean = oldItem.name  == newItem.name

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.iconWebLink == oldItem.iconWebLink

}