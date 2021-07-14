package com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank
import com.gno.erbs.erbs.stats.ui.top.RankDiffUtilCallback

class RankAdapter(   private val cellClickListener: (Int) -> Unit
) : ListAdapter<Rank, RankAdapter.DataHolder>(RankDiffUtilCallback()) {

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        return DataHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rank, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {

        val item = getItem(position)
        holder.rankNumber.text = item.rankNumber.toString()
        holder.nickname.text = item.nickname
        holder.mmr.text = item.mmr.toString()
        Glide.with(holder.rankImage.context).load(item.rankTierImageWebLink).circleCrop().into(holder.rankImage)

        holder.linearLayout.setOnClickListener {
            cellClickListener.invoke(item.userNum)
        }

    }

    class DataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.head)
        val rankImage: ImageView = itemView.findViewById(R.id.rank_image)
        val rankNumber: TextView = itemView.findViewById(R.id.rank_number)
        val nickname: TextView = itemView.findViewById(R.id.nickname)
        val mmr: TextView = itemView.findViewById(R.id.mmr)

    }

}