package com.gno.erbs.erbs.stats.ui.userstats.usercharecters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.databinding.FragmentUserCharactersBinding
import com.gno.erbs.erbs.stats.ui.userstats.UserStatsViewModel


class UserCharactersFragment : Fragment() {

    companion object {
        fun newInstance() = UserCharactersFragment()
    }

    private lateinit var binding: FragmentUserCharactersBinding
    private lateinit var viewModel: UserStatsViewModel
    private val charactersAdapter = UserCharactersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserCharactersBinding.inflate(
            inflater, container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       activity?.let{ activity ->
           charactersAdapter.addLoading()
           viewModel =
               ViewModelProvider(activity.supportFragmentManager.fragments.first().childFragmentManager.fragments[0]).get(
                   UserStatsViewModel::class.java
               )

           viewModel.updateLiveData.observe(viewLifecycleOwner) {
               if (it) {
                   charactersAdapter.submitList(null)
                   charactersAdapter.addLoading()
               }
           }

           viewModel.userCharactersStatsLiveData.observe(viewLifecycleOwner){
               charactersAdapter.submitList(it)
           }
       }
            binding.recyclerViewUserCharacters.adapter = charactersAdapter

        binding.head.imageCharacter.visibility = View.INVISIBLE
        binding.head.name.text = "Name"
        binding.head.totalGames.text = "Total games"
        binding.head.maxKillings.text = "Max killings"
        binding.head.wins.text = "Wins"
        binding.head.top3.text = "Top 3"
        binding.head.averageRank.text = "Average rank"

    }
}