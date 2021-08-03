package com.gno.erbs.erbs.stats.ui.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentTopBinding
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.ui.base.BaseFragment

class TopFragment : BaseFragment() {



    private val viewModel: TopViewModel by lazy {
        ViewModelProvider(this).get(TopViewModel::class.java)
    }


    private lateinit var binding: FragmentTopBinding

    var loading = false

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
        super.onViewCreated(view, savedInstanceState)

        context?.let { contextActivity ->

            rankAdapter.addLoading()

            viewModel.getTopRanks(
                DataRepository.getDefaultMatchingTeamMode(contextActivity),
                DataRepository.getDefaultSeasonId(contextActivity)
            )
        }
        viewModel.ranksLiveData.observe(viewLifecycleOwner) { ranks ->
            rankAdapter.submitList(ranks)
            loading = false
        }


        initRecyclerVIew()

        binding.head.rankNumber.text = "rank"
        binding.head.nickname.text = "nickname"
        binding.head.mmr.text = "mmr"
    }

    private fun initRecyclerVIew() {

        binding.recyclerViewRanks.adapter = rankAdapter

        binding.recyclerViewRanks.addOnScrollListener(object :
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
                        rankAdapter.addLoading()
                        viewModel.loadList(totalItemCount,totalItemCount+20)
                    }
                }
            }
        })

    }

}