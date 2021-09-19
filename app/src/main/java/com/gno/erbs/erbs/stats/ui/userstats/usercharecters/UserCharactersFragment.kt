package com.gno.erbs.erbs.stats.ui.userstats.usercharecters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentUserCharactersBinding
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.userstats.UserStatsViewModel
import javax.inject.Inject


class UserCharactersFragment : Fragment() {

    companion object {
        fun newInstance() = UserCharactersFragment()
    }

    private lateinit var binding: FragmentUserCharactersBinding
    private lateinit var viewModel: UserStatsViewModel

    @Inject
    lateinit var charactersAdapter: UserCharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserCharactersBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onAttach(context: Context) {
        context.activityComponent.fragmentComponent().fragment(this).build().also {
            it.inject(this)
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { activity ->
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

            viewModel.userCharactersStatsLiveData.observe(viewLifecycleOwner) {
                charactersAdapter.submitList(it)
            }
        }
        binding.recyclerViewUserCharacters.adapter = charactersAdapter

        binding.head.imageCharacter.visibility = View.INVISIBLE
        binding.head.name.text = getString(R.string.head_name)
        binding.head.totalGames.text = getString(R.string.head_total_games)
        binding.head.maxKillings.text = getString(R.string.head_max_killings)
        binding.head.wins.text = getString(R.string.head_wins)
        binding.head.top3.text = getString(R.string.head_top3)
        binding.head.averageRank.text = getString(R.string.head_avarage_rank)

    }
}