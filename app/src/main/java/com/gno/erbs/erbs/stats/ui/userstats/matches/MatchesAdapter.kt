package com.gno.erbs.erbs.stats.ui.userstats.matches

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.gno.erbs.erbs.stats.ui.base.BaseAdapter
import com.gno.erbs.erbs.stats.ui.top.RankAdapter
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class MatchesAdapter(
) : BaseAdapter<UserGame, RecyclerView.ViewHolder>(MatchesDiffUtilCallback()) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_MATCH = 2
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
            false -> TYPE_MATCH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item_match, parent, false)
            )
            TYPE_MATCH -> MatchHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_match, parent, false)
            )
            else -> throw RuntimeException("Type does not fit")
        }
    }

    fun addLoading(){
        val currentList = currentList.toMutableList()
        currentList.add(null)
        submitList(currentList)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        when (holder.itemViewType) {

            TYPE_MATCH -> {

                val rankHolder = holder as MatchHolder

                rankHolder.gameRank.text = item.gameRank.toString()
                rankHolder.teamMode.text = item.teamMode.toString()

                rankHolder.date.text =
                    SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(item.date)

                rankHolder.server.text = item.serverName
                rankHolder.kills.text = item.playerKill.toString()
                rankHolder.assistants.text = item.playerAssistant.toString()
                rankHolder.hunter.text = item.monsterKill.toString()
                rankHolder.mmr.text = item.mmr.toString()

                item.characterImageWebLink?.let {
                    Glide.with(rankHolder.character.context).load(it).circleCrop()
                        .into(rankHolder.character)
                }

                item.weaponTypeImageWebLink?.let {
                    Glide.with(rankHolder.characterWeapon.context).load(it).circleCrop()
                        .into(rankHolder.characterWeapon)
                }

                //companyImage

                if (item.equipment.item1WebLink.isNotEmpty()) {
                    loadImage(rankHolder.item1, item.equipment.item1WebLink)
                }

                if (item.equipment.item2WebLink.isNotEmpty()) {
                    loadImage(rankHolder.item2, item.equipment.item2WebLink)
                }

                if (item.equipment.item3WebLink.isNotEmpty()) {
                    loadImage(rankHolder.item3, item.equipment.item3WebLink)
                }

                if (item.equipment.item4WebLink.isNotEmpty()) {
                    loadImage(rankHolder.item4, item.equipment.item4WebLink)
                }

                if (item.equipment.item5WebLink.isNotEmpty()) {
                    loadImage(rankHolder.item5, item.equipment.item5WebLink)
                }

                if (item.equipment.item6WebLink.isNotEmpty()) {
                    loadImage(rankHolder.item6, item.equipment.item6WebLink)
                }

            }
        }
    }

    private fun loadImage(view: ImageView,webLink: String){
        Glide.with(view.context).load(webLink)
            .placeholder(createShimmer(view.context))
            .error(R.drawable.loading_image)
            .into(view)
    }

    class MatchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val gameRank: TextView = itemView.findViewById(R.id.game_rank)
        val teamMode: TextView = itemView.findViewById(R.id.team_mode)
        val date: TextView = itemView.findViewById(R.id.date)
        val server: TextView = itemView.findViewById(R.id.server)
        val character: ImageView = itemView.findViewById(R.id.character)
        val characterWeapon: ImageView = itemView.findViewById(R.id.character_weapon)
        val kills: TextView = itemView.findViewById(R.id.kills)
        val assistants: TextView = itemView.findViewById(R.id.assistants)
        val hunter: TextView = itemView.findViewById(R.id.hunter)
        val mmr: TextView = itemView.findViewById(R.id.mmr)
        val item1: ImageView = itemView.findViewById(R.id.item1)
        val item2: ImageView = itemView.findViewById(R.id.item2)
        val item3: ImageView = itemView.findViewById(R.id.item3)
        val item4: ImageView = itemView.findViewById(R.id.item4)
        val item5: ImageView = itemView.findViewById(R.id.item5)
        val item6: ImageView = itemView.findViewById(R.id.item6)
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}