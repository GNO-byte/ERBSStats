package com.gno.erbs.erbs.stats.ui.userstats.usercharecters

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.erbs.userstats.CharacterStat

class UserCharactersDiffUtilCallback : DiffUtil.ItemCallback<CharacterStat>() {

    override fun areItemsTheSame(oldItem: CharacterStat, newItem: CharacterStat): Boolean = oldItem === newItem

    override fun areContentsTheSame(oldItem: CharacterStat, newItem: CharacterStat): Boolean =
        oldItem == newItem

}