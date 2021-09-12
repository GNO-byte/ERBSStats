package com.gno.erbs.erbs.stats.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.MenuObject

class HomeAdapterDiffUtilCallback : DiffUtil.ItemCallback<MenuObject>() {

    override fun areItemsTheSame(oldItem: MenuObject, newItem: MenuObject): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: MenuObject, newItem: MenuObject): Boolean =
        oldItem.navigationLink == oldItem.navigationLink
}