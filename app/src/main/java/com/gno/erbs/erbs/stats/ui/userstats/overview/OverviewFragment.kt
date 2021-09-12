package com.gno.erbs.erbs.stats.ui.userstats.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentOverviewBinding
import com.gno.erbs.erbs.stats.model.TeamMode
import com.gno.erbs.erbs.stats.model.Tier
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import com.gno.erbs.erbs.stats.ui.userstats.UserStatsViewModel
import java.text.DecimalFormat
import kotlin.math.roundToInt


class OverviewFragment : BaseFragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var binding: FragmentOverviewBinding
    private lateinit var viewModel: UserStatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOverviewBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let { activity ->

            binding.overview.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE

            viewModel =
                ViewModelProvider(activity.supportFragmentManager.fragments.first().childFragmentManager.fragments[0]).get(
                    UserStatsViewModel::class.java
                )

            viewModel.updateLiveData.observe(viewLifecycleOwner) {
                if (it) {
                    binding.overview.visibility = View.GONE
                    binding.loading.visibility = View.VISIBLE
                }
            }

            viewModel.userStatsLiveData.observe(viewLifecycleOwner) { usersStatsList ->

                binding.overview.removeAllViews()

                usersStatsList?.forEach {

                    val itemGameMode =
                        layoutInflater.inflate(
                            R.layout.item_game_mode,
                            binding.root,
                            false
                        ) as ConstraintLayout

                    val tierImage: ImageView = itemGameMode.findViewById(R.id.tier_image)
                    val loading: ShimmerFrameLayout = itemGameMode.findViewById(R.id.loading)

                    val mmr: TextView = itemGameMode.findViewById(R.id.mmr)
                    val rank: TextView = itemGameMode.findViewById(R.id.rank)
                    val top: TextView = itemGameMode.findViewById(R.id.top)
                    val parameters: LinearLayout = itemGameMode.findViewById(R.id.parameters)
                    val teamMode: TextView = itemGameMode.findViewById(R.id.team_mode)


                    tierImage.contentDescription = Tier.findByMmr(it.mmr).title

                    tierImage.visibility = View.GONE
                    loading.visibility = View.VISIBLE

                    loadImage(tierImage, it.rankTierImageWebLink, loading)

                    mmr.text = it.mmr.toString()
                    rank.text = it.rank.toString()
                    top.text =
                        DecimalFormat("#.##").format(it.rank.toFloat() / it.rankSize.toFloat() * 100.0)
                    teamMode.text = TeamMode.findByValue(it.matchingTeamMode).title

                    parameters.createParameter(
                        layoutInflater,
                        "Average rank",
                        it.averageRank.toInt(),
                        getMaxValueAverageRank(
                            it.matchingTeamMode
                        )
                    )
                    parameters.createParameter(
                        layoutInflater,
                        "Average kills",
                        it.averageKills.toInt(),
                        5
                    )
                    parameters.createParameter(
                        layoutInflater,
                        "Average assistants",
                        it.averageAssistants.toInt(),
                        5
                    )
                    parameters.createParameter(
                        layoutInflater,
                        "Average hunts",
                        it.averageHunts.toInt(),
                        it.averageHunts.toInt()
                    )
                    parameters.createParameter(
                        layoutInflater, "Top 1",
                        (it.top1 * 100).roundToInt(), 100
                    )
                    parameters.createParameter(
                        layoutInflater, "Top 2",
                        (it.top2 * 100).roundToInt(), 100
                    )
                    parameters.createParameter(
                        layoutInflater, "Top 3",
                        (it.top3 * 100).roundToInt(), 100
                    )
                    parameters.createParameter(
                        layoutInflater,
                        "Total games",
                        it.totalGames,
                        it.totalGames
                    )

                    binding.overview.addView(itemGameMode)

                }
                binding.loading.visibility = View.GONE
                binding.overview.visibility = View.VISIBLE
            }
        }
    }

    private fun getMaxValueAverageRank(matchingTeamMode: Int) = when (matchingTeamMode) {
        1 -> 18
        2 -> 9
        3 -> 6
        else -> 0
    }


    private fun LinearLayout.createParameter(
        layoutInflater: LayoutInflater,
        name: String,
        value: Int,
        maxValue: Int
    ) {

        val itemParameter =
            layoutInflater.inflate(R.layout.item_parameter, this, false) as LinearLayout
        val nameView: TextView = itemParameter.findViewById(R.id.name)
        val valueView: TextView = itemParameter.findViewById(R.id.value)
        val progressView: ProgressBar = itemParameter.findViewById(R.id.progress)

        nameView.text = name
        valueView.text = value.toString()
        progressView.progress = value
        progressView.max = maxValue

        this.addView(itemParameter)

    }
}



