package com.gno.erbs.erbs.stats.ui.home.slider

import androidx.recyclerview.widget.DiffUtil

class HomeSlideAdapterDiffUtilCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == oldItem
}