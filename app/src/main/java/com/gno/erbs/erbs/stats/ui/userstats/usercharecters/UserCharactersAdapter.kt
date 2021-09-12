package com.gno.erbs.erbs.stats.ui.userstats.usercharecters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.userstats.CharacterStat
import com.gno.erbs.erbs.stats.ui.base.BaseListAdapter


class UserCharactersAdapter :
    BaseListAdapter<CharacterStat, RecyclerView.ViewHolder>(UserCharactersDiffUtilCallback()) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_HEAD = 2
        private const val TYPE_CHARACTER = 3
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
            false -> when (item.isHead) {
                true -> TYPE_HEAD
                false -> TYPE_CHARACTER
            }
        }
    }

    fun addLoading() {
        val currentList = currentList.toMutableList()
        currentList.add(null)
        currentList.add(null)
        currentList.add(null)
        submitList(currentList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item_user_character, parent, false)
            )
            TYPE_HEAD -> HeadHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_character_teammode, parent, false)
            )

            TYPE_CHARACTER -> CharacterHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_character, parent, false)
            )
            else -> throw RuntimeException("The type has to be HEAD or CHARACTER")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        when (holder.itemViewType) {
            TYPE_HEAD -> {
                val headHolder = holder as HeadHolder
                headHolder.name.text = item.headName
            }
            TYPE_CHARACTER -> {
                val characterHolder = holder as CharacterHolder

                characterHolder.image.visibility = View.GONE
                characterHolder.loading.visibility = View.VISIBLE

                loadImage(characterHolder.image, item.WebLinkImage, characterHolder.loading)

                characterHolder.name.text = item.characterName
                characterHolder.totalGames.text = item.totalGames.toString()
                characterHolder.maxKillings.text = item.maxKillings.toString()
                characterHolder.wins.text = item.wins.toString()
                characterHolder.top3.text = item.top3.toString()
                characterHolder.averageRank.text = item.averageRank.toString()
            }
        }
    }

    class CharacterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image_character)
        val loading: ShimmerFrameLayout = itemView.findViewById(R.id.loading)
        val name: TextView = itemView.findViewById(R.id.name)
        val totalGames: TextView = itemView.findViewById(R.id.total_games)
        val maxKillings: TextView = itemView.findViewById(R.id.max_killings)
        val wins: TextView = itemView.findViewById(R.id.wins)
        val top3: TextView = itemView.findViewById(R.id.top3)
        val averageRank: TextView = itemView.findViewById(R.id.averageRank)
    }

    class HeadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}