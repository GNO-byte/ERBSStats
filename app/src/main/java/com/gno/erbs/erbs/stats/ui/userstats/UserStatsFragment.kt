package com.gno.erbs.erbs.stats.ui.userstats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.databinding.FragmentUserStatsBinding
import com.gno.erbs.erbs.stats.repository.DataRepository

class UserStatsFragment : Fragment() {

    private val viewModel: UserStatsViewModel by lazy {
        ViewModelProvider(this).get(UserStatsViewModel::class.java)
    }

    private lateinit var binding: FragmentUserStatsBinding

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

        activity?.let { thisActivity ->
            viewModel.loadUserStats(
                (arguments?.getInt("code", 1) ?: DataRepository.getDefaultUserNumber(thisActivity)).toString(),
                DataRepository.getDefaultSeasonId(thisActivity)
            )

            viewModel.userStatsLiveData.observe(viewLifecycleOwner) { usersStats ->
                binding.name.text = usersStats[0].nickname

                context?.let { thisContext ->
                    Glide.with(thisContext).load(usersStats[0].topCharacterHalfImageWebLink)
                        .into(binding.topCharacterImage)
                }

            }

            binding.pager.adapter =
                ViewPagerAdapter(thisActivity.supportFragmentManager, requireContext())
            binding.tabs.setupWithViewPager(binding.pager)
        }
    }

}