package com.gno.erbs.erbs.stats.ui.search

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.FoundObject

class SearchDiffUtilCallback : DiffUtil.ItemCallback<FoundObject>() {

    override fun areItemsTheSame(oldItem: FoundObject, newItem: FoundObject): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: FoundObject, newItem: FoundObject): Boolean =
        oldItem.code == oldItem.code
}