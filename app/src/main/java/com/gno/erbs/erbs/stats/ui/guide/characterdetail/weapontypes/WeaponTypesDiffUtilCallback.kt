package com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank

class WeaponTypesDiffUtilCallback : DiffUtil.ItemCallback<WeaponType>() {

    override fun areItemsTheSame(oldItem: WeaponType, newItem: WeaponType): Boolean = oldItem === newItem

    override fun areContentsTheSame(oldItem: WeaponType, newItem: WeaponType): Boolean =
        oldItem == newItem

}