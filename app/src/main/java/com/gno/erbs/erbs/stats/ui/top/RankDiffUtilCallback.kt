package com.gno.erbs.erbs.stats.ui.top

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank
import javax.inject.Inject

class RankDiffUtilCallback @Inject constructor() : DiffUtil.ItemCallback<Rank>() {

    override fun areItemsTheSame(oldItem: Rank, newItem: Rank): Boolean = oldItem === newItem

    override fun areContentsTheSame(oldItem: Rank, newItem: Rank): Boolean =
        oldItem == newItem

}