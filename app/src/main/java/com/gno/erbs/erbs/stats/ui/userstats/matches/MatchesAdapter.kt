package com.gno.erbs.erbs.stats.ui.userstats.matches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class MatchesAdapter(
) : ListAdapter<UserGame, MatchesAdapter.DataHolder>(MatchesDiffUtilCallback()) {

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        return DataHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_match, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {

        val item = getItem(position)
        holder.gameRank.text = item.gameRank.toString()
        holder.teamMode.text = item.teamMode.toString()

        holder.date.text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(item.date)

        holder.server.text = item.serverName
        holder.kills.text = item.playerKill.toString()
        holder.assistants.text = item.playerAssistant.toString()
        holder.hunter.text = item.monsterKill.toString()
        holder.mmr.text = item.mmr.toString()

        item.characterImageWebLink?.let{
            Glide.with(holder.character.context).load(it).circleCrop()
                .into(holder.character)
        }

        item.weaponTypeImageWebLink?.let{
            Glide.with(holder.characterWeapon.context).load(it).circleCrop()
                .into(holder.characterWeapon)
        }

        //companyImage

        if (item.equipment.item1WebLink.isNotEmpty()) {
            Glide.with(holder.item1.context).load(item.equipment.item1WebLink)
                .into(holder.item1)
        }

        if (item.equipment.item2WebLink.isNotEmpty()) {
            Glide.with(holder.item2.context).load(item.equipment.item2WebLink)
                .into(holder.item2)
        }

        if (item.equipment.item3WebLink.isNotEmpty()) {
            Glide.with(holder.item3.context).load(item.equipment.item3WebLink)
                .into(holder.item3)

        }

        if (item.equipment.item4WebLink.isNotEmpty()) {
            Glide.with(holder.item4.context).load(item.equipment.item4WebLink)
                .into(holder.item4)
        }

        if (item.equipment.item5WebLink.isNotEmpty()) {
            Glide.with(holder.item5.context).load(item.equipment.item5WebLink)
                .into(holder.item5)
        }

        if (item.equipment.item6WebLink.isNotEmpty()) {
            Glide.with(holder.item6.context).load(item.equipment.item6WebLink)
                .into(holder.item6)
        }

    }

    class DataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

}