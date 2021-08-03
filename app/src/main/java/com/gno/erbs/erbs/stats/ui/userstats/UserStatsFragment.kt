package com.gno.erbs.erbs.stats.ui.userstats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentUserStatsBinding
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.base.BaseFragment

class UserStatsFragment : BaseFragment() {

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
        super.onViewCreated(view, savedInstanceState)

        activity?.let { thisActivity ->

            binding.name.visibility = View.GONE
            binding.loadingName.visibility = View.VISIBLE

            binding.topCharacterImage.visibility = View.GONE
            binding.loadingImage.visibility = View.VISIBLE


            val foundIdInt = arguments?.getInt("code", 0) ?: 0
            var foundId = ""
            if (foundIdInt == 0) {
                foundId = DataRepository.getDefaultUserNumber(thisActivity)
            } else {
                foundId = foundIdInt.toString()
                DataRepository.setDefaultUserNumber(thisActivity, foundId)
            }

            val seasonId = DataRepository.getDefaultSeasonId(thisActivity)

            viewModel.loadUserStats(foundId, seasonId)

            viewModel.userStatsLiveData.observe(viewLifecycleOwner) { usersStats ->
                binding.name.text = usersStats[0].nickname

                binding.loadingName.visibility = View.GONE
                binding.name.visibility = View.VISIBLE

                context?.let { thisContext ->
                    Glide.with(thisContext).load(usersStats[0].topCharacterHalfImageWebLink)
                        .placeholder(createShimmer(thisContext))
                        .error(R.drawable.loading_image)
                        .into(binding.topCharacterImage)

                    binding.loadingImage.visibility = View.GONE
                    binding.topCharacterImage.visibility = View.VISIBLE

                }

            }

            binding.pager.adapter =
                ViewPagerAdapter(thisActivity.supportFragmentManager, requireContext())
            binding.tabs.setupWithViewPager(binding.pager)
        }
    }

}