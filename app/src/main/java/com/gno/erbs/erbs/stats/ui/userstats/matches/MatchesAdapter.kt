package com.gno.erbs.erbs.stats.ui.userstats.matches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame
import com.gno.erbs.erbs.stats.ui.LoadingImageHelper
import com.gno.erbs.erbs.stats.ui.base.BaseListAdapter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MatchesAdapter @Inject constructor(loadingImageHelper: LoadingImageHelper) :
    BaseListAdapter<UserGame, RecyclerView.ViewHolder>(
        MatchesDiffUtilCallback(),
        loadingImageHelper
    ) {

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

    fun addLoading() {
        val currentList = currentList.toMutableList()
        currentList.add(null)
        submitList(currentList)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        when (holder.itemViewType) {

            TYPE_MATCH -> {

                val rankHolder = holder as MatchHolder

                rankHolder.character.visibility = View.GONE
                rankHolder.characterWeapon.visibility = View.GONE

                rankHolder.item1.visibility = View.GONE
                rankHolder.item2.visibility = View.GONE
                rankHolder.item3.visibility = View.GONE
                rankHolder.item4.visibility = View.GONE
                rankHolder.item5.visibility = View.GONE
                rankHolder.item6.visibility = View.GONE

                rankHolder.loadingItem1.visibility = View.VISIBLE
                rankHolder.loadingItem2.visibility = View.VISIBLE
                rankHolder.loadingItem3.visibility = View.VISIBLE
                rankHolder.loadingItem4.visibility = View.VISIBLE
                rankHolder.loadingItem5.visibility = View.VISIBLE
                rankHolder.loadingItem6.visibility = View.VISIBLE

                rankHolder.gameRank.text = item.gameRank.toString()
                rankHolder.teamMode.text = item.teamMode.toString()

                rankHolder.date.text = item.date?.let {
                    SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it)
                }

                rankHolder.server.text = item.serverName
                rankHolder.kills.text = item.playerKill.toString()
                rankHolder.assistants.text = item.playerAssistant.toString()
                rankHolder.hunter.text = item.monsterKill.toString()
                // rankHolder.mmr.text = item.mmr.toString()

                item.characterImageWebLink?.let {
                    loadImage(
                        rankHolder.character,
                        it,
                        rankHolder.loadingCharacter
                    )
                }

                item.weaponTypeImageWebLink?.let {
                    loadImage(
                        rankHolder.characterWeapon,
                        it,
                        rankHolder.loadingCharacterWeapon
                    )
                }

                //companyImage
                loadingItem(rankHolder.item1, item.equipment?.item1WebLink, rankHolder.loadingItem1)
                loadingItem(rankHolder.item2, item.equipment?.item2WebLink, rankHolder.loadingItem2)
                loadingItem(rankHolder.item3, item.equipment?.item3WebLink, rankHolder.loadingItem3)
                loadingItem(rankHolder.item4, item.equipment?.item4WebLink, rankHolder.loadingItem4)
                loadingItem(rankHolder.item5, item.equipment?.item5WebLink, rankHolder.loadingItem5)
                loadingItem(rankHolder.item6, item.equipment?.item6WebLink, rankHolder.loadingItem6)
            }
        }
    }

    private fun loadingItem(
        view: ImageView, webLink: String?,
        loading: View
    ) {
        if (webLink != "noItem") {
            loadImage(
                view,
                webLink,
                loading
            )
        } else {
            view.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
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

        //val mmr: TextView = itemView.findViewById(R.id.mmr)
        val item1: ImageView = itemView.findViewById(R.id.item1)
        val item2: ImageView = itemView.findViewById(R.id.item2)
        val item3: ImageView = itemView.findViewById(R.id.item3)
        val item4: ImageView = itemView.findViewById(R.id.item4)
        val item5: ImageView = itemView.findViewById(R.id.item5)
        val item6: ImageView = itemView.findViewById(R.id.item6)

        val loadingCharacter: ShimmerFrameLayout = itemView.findViewById(R.id.loading_character)
        val loadingCharacterWeapon: ShimmerFrameLayout =
            itemView.findViewById(R.id.loading_character_weapon)
        val loadingItem1: ShimmerFrameLayout = itemView.findViewById(R.id.loading_item1)
        val loadingItem2: ShimmerFrameLayout = itemView.findViewById(R.id.loading_item2)
        val loadingItem3: ShimmerFrameLayout = itemView.findViewById(R.id.loading_item3)
        val loadingItem4: ShimmerFrameLayout = itemView.findViewById(R.id.loading_item4)
        val loadingItem5: ShimmerFrameLayout = itemView.findViewById(R.id.loading_item5)
        val loadingItem6: ShimmerFrameLayout = itemView.findViewById(R.id.loading_item6)
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}