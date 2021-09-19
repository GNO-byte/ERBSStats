package com.gno.erbs.erbs.stats.ui.userstats

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentUserStatsBinding
import com.gno.erbs.erbs.stats.model.Season
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class UserStatsFragment : BaseFragment() {

    private val foundId by lazy {
        arguments?.getInt("code", 0).let {
            if (it == 0) null else it.toString()
        }
    }

    private val seasonId by lazy { arguments?.getString("seasonId", null) }

    private val name by lazy { arguments?.getString("name", "") }

    @Inject
    lateinit var factory: UserStatsViewModel.UserStatsViewModelFactory.Factory
    private val viewModel: UserStatsViewModel by viewModels {
        factory.create(foundId, seasonId)
    }

    private lateinit var binding: FragmentUserStatsBinding
    var loading = false

    override fun onAttach(context: Context) {
        context.activityComponent.fragmentComponent().fragment(this).build().also {
            it.inject(this)
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserStatsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->

            binding.topCharacterImage.visibility = View.GONE
            binding.loadingImage.visibility = View.VISIBLE

            binding.name.text = name

            loading = true

            initSeasonSpinner(activity, binding.season) { seasonName, context ->
                changeSeason(seasonName, context)
            }

            viewModel.userStatsLiveData.observe(viewLifecycleOwner) { userStats ->
                loading = false
                userStats?.let { usersStats ->

                    binding.name.text = usersStats[0].nickname

                    loadImage(
                        binding.topCharacterImage,
                        usersStats[0].topCharacterHalfImageWebLink,
                        binding.loadingImage
                    )

                } ?: (activity as MainActivity).showConnectionError {
                    viewModel.loadUserStats()
                }
            }

            binding.pager.adapter =
                ViewPagerAdapter(childFragmentManager, lifecycle)

            TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
                tab.text =
                    (binding.pager.adapter as ViewPagerAdapter).getPageTitle(position)
            }.attach()

        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun initSeasonSpinner(
        context: Context,
        spinner: Spinner,
        run: (String, Context) -> Unit
    ) {
        spinner.adapter = ArrayAdapter(
            context,
            R.layout.item_season,
            Season.values().map { it.title }).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        viewModel.getCurrentSeason()?.let {
            spinner.setSelection(
                (spinner.adapter as ArrayAdapter<String>).getPosition(
                    it.title
                )
            )
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!loading) {
                    parent?.getItemAtPosition(position)?.let {
                        run.invoke(it as String, context)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                ///
            }
        }
    }

    private fun changeSeason(seasonName: String, context: Context) {
        binding.topCharacterImage.visibility = View.GONE
        binding.loadingImage.visibility = View.VISIBLE
        viewModel.changeSeason(seasonName)
    }

}