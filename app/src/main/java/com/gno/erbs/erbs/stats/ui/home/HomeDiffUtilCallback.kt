package com.gno.erbs.erbs.stats.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.MenuObject

class HomeDiffUtilCallback : DiffUtil.ItemCallback<MenuObject>() {

    override fun areItemsTheSame(oldItem: MenuObject, newItem: MenuObject): Boolean = oldItem.name  == newItem.name

    override fun areContentsTheSame(oldItem: MenuObject, newItem: MenuObject): Boolean =
        oldItem.navigationLink == oldItem.navigationLink
}