package com.gno.erbs.erbs.stats.ui.userstats.matches

import androidx.recyclerview.widget.DiffUtil
import com.gno.erbs.erbs.stats.model.erbs.matches.UserGame

class MatchesDiffUtilCallback : DiffUtil.ItemCallback<UserGame>() {

    override fun areItemsTheSame(oldItem: UserGame, newItem: UserGame): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: UserGame, newItem: UserGame): Boolean {

        return oldItem.gameRank == newItem.gameRank &&
                oldItem.teamMode == newItem.teamMode &&
                oldItem.date == newItem.date &&
                oldItem.serverName == newItem.serverName &&
                oldItem.playerKill == newItem.playerKill &&
                oldItem.playerAssistant == newItem.playerAssistant &&
                oldItem.monsterKill == newItem.monsterKill &&
                oldItem.mmr == newItem.mmr &&
                oldItem.equipment?.item1WebLink == newItem.equipment?.item1WebLink &&
                oldItem.equipment?.item2WebLink == newItem.equipment?.item2WebLink &&
                oldItem.equipment?.item3WebLink == newItem.equipment?.item3WebLink &&
                oldItem.equipment?.item4WebLink == newItem.equipment?.item4WebLink &&
                oldItem.equipment?.item5WebLink == newItem.equipment?.item5WebLink
    }
}