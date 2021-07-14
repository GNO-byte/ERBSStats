package com.gno.erbs.erbs.stats.ui.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentTopBinding
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes.RankAdapter

class TopFragment : Fragment() {

    private val viewModel: TopViewModel by lazy {
        ViewModelProvider(this).get(TopViewModel::class.java)
    }


    private lateinit var binding: FragmentTopBinding
    private val rankAdapter = RankAdapter { code ->
        val bundle = bundleOf("code" to code)
        findNavController().navigate(R.id.nav_user_stats, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTopBinding.inflate(
            inflater, container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let { contextActivity ->
            viewModel.getTopRanks(
                DataRepository.getDefaultMatchingTeamMode(contextActivity),
                DataRepository.getDefaultSeasonId(contextActivity)
            )
        }
        viewModel.ranksLiveData.observe(viewLifecycleOwner) { ranks ->
            rankAdapter.submitList(ranks)
        }

        initRecyclerVIew()

        binding.head.rankNumber.text = "rank"
        binding.head.nickname.text = "nickname"
        binding.head.mmr.text = "mmr"
    }

    private fun initRecyclerVIew() {

        binding.recyclerViewRanks.adapter = rankAdapter

    }

}