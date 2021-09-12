package com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill

class SkillsDiffUtilCallback : DiffUtil.ItemCallback<CoreSkill>() {

    override fun areItemsTheSame(oldItem: CoreSkill, newItem: CoreSkill): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: CoreSkill, newItem: CoreSkill): Boolean =
        oldItem == newItem

}