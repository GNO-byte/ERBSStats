package com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank

class SkillsDiffUtilCallback : DiffUtil.ItemCallback<CoreSkill>() {

    override fun areItemsTheSame(oldItem: CoreSkill, newItem: CoreSkill): Boolean = oldItem === newItem

    override fun areContentsTheSame(oldItem: CoreSkill, newItem: CoreSkill): Boolean =
        oldItem == newItem

}