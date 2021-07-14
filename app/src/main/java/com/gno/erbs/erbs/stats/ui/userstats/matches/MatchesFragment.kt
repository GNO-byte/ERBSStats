package com.gno.erbs.erbs.stats.ui.userstats.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.databinding.FragmentMatchesBinding
import com.gno.erbs.erbs.stats.ui.userstats.UserStatsViewModel


class MatchesFragment : Fragment() {

    companion object {
        fun newInstance() = MatchesFragment()
    }

    private lateinit var binding: FragmentMatchesBinding
    private lateinit var viewModel: UserStatsViewModel
    private val matchesAdapter = MatchesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchesBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { thisActivity ->

            viewModel =
                ViewModelProvider(thisActivity.supportFragmentManager.fragments.first().childFragmentManager.fragments[0]).get(
                    UserStatsViewModel::class.java
                )

            viewModel.userGamesLiveData.observe(viewLifecycleOwner) {
                matchesAdapter.submitList(it)
            }

            binding.recyclerViewMatches.adapter = matchesAdapter
        }
    }
}