package com.gno.erbs.erbs.stats.ui.top

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.rank.Rank

class RankAdapter(
    private val cellClickListener: (Int) -> Unit
) : ListAdapter<Rank, RecyclerView.ViewHolder>(RankDiffUtilCallback()) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_RANK = 2
    }

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item == null) {
            true -> TYPE_LOADING
            false -> TYPE_RANK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item_rank, parent, false)
            )
            TYPE_RANK -> RankHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rank, parent, false)
            )
            else -> throw RuntimeException("Type does not fit")
        }
    }

    fun addLoading(){
        val currentList = currentList.toMutableList()
        currentList.add(null)
        currentList.add(null)
        currentList.add(null)
        submitList(currentList)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder.itemViewType) {

            TYPE_RANK -> {

                val rankHolder = holder as RankHolder
                rankHolder.rankNumber.text = item.rankNumber.toString()
                rankHolder.nickname.text = item.nickname
                rankHolder.mmr.text = item.mmr.toString()
                Glide.with(rankHolder.rankImage.context).load(item.rankTierImageWebLink)
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.loading_image)
                    .circleCrop()
                    .into(rankHolder.rankImage)

                rankHolder.linearLayout.setOnClickListener {
                    cellClickListener.invoke(item.userNum)
                }

            }
        }

    }

    class RankHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.head)
        val rankImage: ImageView = itemView.findViewById(R.id.rank_image)
        val rankNumber: TextView = itemView.findViewById(R.id.rank_number)
        val nickname: TextView = itemView.findViewById(R.id.nickname)
        val mmr: TextView = itemView.findViewById(R.id.mmr)
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}