package com.gno.erbs.erbs.stats.ui.userstats.usercharecters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.userstats.CharacterStat

class UserCharactersAdapter : ListAdapter<CharacterStat, UserCharactersAdapter.DataHolder>(UserCharactersDiffUtilCallback()) {

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        return DataHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_character, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {

        val item = getItem(position)

        Glide.with(holder.image.context).load(item.WebLinkImage).circleCrop()
            .into(  holder.image)

        holder.name.text = item.characterName
        holder.totalGames.text = item.totalGames.toString()
        holder.maxKillings.text = item.maxKillings.toString()
        holder.wins.text = item.wins.toString()
        holder.top3.text = item.top3.toString()
        holder.averageRank.text = item.averageRank.toString()

    }

    class DataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image_character)
        val name: TextView = itemView.findViewById(R.id.name)
        val totalGames: TextView = itemView.findViewById(R.id.total_games)
        val maxKillings: TextView = itemView.findViewById(R.id.max_killings)
        val wins: TextView = itemView.findViewById(R.id.wins)
        val top3: TextView = itemView.findViewById(R.id.top3)
        val averageRank: TextView = itemView.findViewById(R.id.averageRank)
    }

}