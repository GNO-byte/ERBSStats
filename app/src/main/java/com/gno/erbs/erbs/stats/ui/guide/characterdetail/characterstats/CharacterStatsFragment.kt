package com.gno.erbs.erbs.stats.ui.guide.characterdetail.characterstats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentCharacterStatsBinding
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.CharacterDetailViewModel

class CharacterStatsFragment : Fragment() {

    companion object {
        fun newInstance() = CharacterStatsFragment()
    }

    private lateinit var binding: FragmentCharacterStatsBinding
    private lateinit var viewModel: CharacterDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCharacterStatsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.overview.visibility = View.GONE
        binding.loading.visibility = View.VISIBLE

        activity?.let { activity ->
            viewModel =
                ViewModelProvider(
                    activity.supportFragmentManager.fragments.first()
                        .childFragmentManager.fragments[0]
                ).get(
                    CharacterDetailViewModel::class.java
                )
        }

        viewModel.characterStatsLiveData.observe(viewLifecycleOwner) { characterStats ->

            characterStats?.let {
                characterStats
                binding.overview.createParameter(
                    layoutInflater,
                    "Parameter",
                    "min lvl",
                    "max lvl"
                )

                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.attackPower.name,
                    characterStats.attackPower.minLvlValue,
                    characterStats.attackPower.maxLvlValue
                )

                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.attackSpeed.name,
                    characterStats.attackSpeed.minLvlValue,
                    characterStats.attackSpeed.maxLvlValue
                )

                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.criticalChance.name,
                    characterStats.criticalChance.minLvlValue,
                    characterStats.criticalChance.maxLvlValue
                )

                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.defence.name,
                    characterStats.defence.minLvlValue,
                    characterStats.defence.maxLvlValue
                )

                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.hpRegen.name,
                    characterStats.hpRegen.minLvlValue,
                    characterStats.hpRegen.maxLvlValue
                )


                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.maxHp.name,
                    characterStats.maxHp.minLvlValue,
                    characterStats.maxHp.maxLvlValue
                )

                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.maxSp.name,
                    characterStats.maxSp.minLvlValue,
                    characterStats.maxSp.maxLvlValue
                )

                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.moveSpeed.name,
                    characterStats.moveSpeed.minLvlValue,
                    characterStats.moveSpeed.maxLvlValue
                )

                binding.overview.createParameter(
                    layoutInflater,
                    characterStats.spRegen.name,
                    characterStats.spRegen.minLvlValue,
                    characterStats.spRegen.maxLvlValue
                )

                binding.overview.createParameter(
                    layoutInflater,
                    "Attack speed limit",
                    characterStats.attackSpeedLimit,
                    null
                )

                binding.overview.createParameter(
                    layoutInflater,
                    "Attack speed min",
                    characterStats.attackSpeedMin,
                    null
                )


                binding.overview.createParameter(
                    layoutInflater,
                    "Radius",
                    characterStats.radius,
                    null
                )

                binding.overview.createParameter(
                    layoutInflater,
                    "Sight Range",
                    characterStats.radius,
                    null
                )

                binding.loading.visibility = View.GONE
                binding.overview.visibility = View.VISIBLE

            }
        }
    }

    private fun <T> LinearLayout.createParameter(
        layoutInflater: LayoutInflater,
        name: String,
        minLvlValue: T?,
        maxLvlValue: T?
    ) {

        val itemParameter =
            layoutInflater.inflate(
                R.layout.item_character_stats_parameter,
                null,
                false
            ) as LinearLayout
        val nameView: TextView = itemParameter.findViewById(R.id.name)
        val minLvlValueView: TextView = itemParameter.findViewById(R.id.min_lvl_value)
        val maxLvlValueView: TextView = itemParameter.findViewById(R.id.max_lvl_value)

        nameView.text = name
        minLvlValueView.text = minLvlValue?.toString() ?: ""
        maxLvlValueView.text = maxLvlValue?.toString() ?: ""

        this.addView(itemParameter)

    }
}