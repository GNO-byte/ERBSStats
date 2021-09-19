package com.gno.erbs.erbs.stats.ui.userstats.matches

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gno.erbs.erbs.stats.databinding.FragmentMatchesBinding
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.userstats.UserStatsViewModel
import javax.inject.Inject


class MatchesFragment : Fragment() {

    companion object {
        fun newInstance() = MatchesFragment()
    }

    private lateinit var binding: FragmentMatchesBinding
    private lateinit var viewModel: UserStatsViewModel

    @Inject
    lateinit var  matchesAdapter: MatchesAdapter
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchesBinding.inflate(
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
            matchesAdapter.addLoading()

            viewModel =
                ViewModelProvider(activity.supportFragmentManager.fragments.first().childFragmentManager.fragments[0]).get(
                    UserStatsViewModel::class.java
                )

            matchesAdapter.addLoading()

            viewModel =
                ViewModelProvider(activity.supportFragmentManager.fragments.first().childFragmentManager.fragments[0]).get(
                    UserStatsViewModel::class.java
                )

            viewModel.updateLiveData.observe(viewLifecycleOwner) {
                if (it) {
                    loading = true
                    matchesAdapter.submitList(null)
                    matchesAdapter.addLoading()
                }
            }

            viewModel.userGamesLiveData.observe(viewLifecycleOwner) {
                matchesAdapter.submitList(it)
                loading = false
            }

            binding.recyclerViewMatches.adapter = matchesAdapter
            binding.recyclerViewMatches.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 && !loading) //check for scroll down
                    {
                        val visibleItemCount =
                            recyclerView.layoutManager?.childCount ?: 0
                        val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                        val firstVisibleItems =
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                        if ((visibleItemCount + firstVisibleItems) >= totalItemCount
                            && firstVisibleItems >= 0
                        ) {
                            loading = true
                            matchesAdapter.addLoading()
                            viewModel.loadMatches()
                        }
                    }
                }
            }
            )
        }
    }
}