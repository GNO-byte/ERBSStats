package com.gno.erbs.erbs.stats.ui.top

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.appComponent
import com.gno.erbs.erbs.stats.databinding.FragmentTopBinding
import com.gno.erbs.erbs.stats.model.Season
import com.gno.erbs.erbs.stats.repository.NavigateHelper
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import javax.inject.Inject


class TopFragment : BaseFragment() {

    lateinit var binding: FragmentTopBinding

    @Inject
    lateinit var factory: TopViewModel.TopViewModelFactory.Factory
    private val viewModel: TopViewModel by viewModels {
        factory.create(null, null)
    }

    private val rankAdapter = RankAdapter { code, name ->
        val bundle = bundleOf("code" to code, "name" to name)
        NavigateHelper.go(findNavController(), R.id.nav_user_stats, bundle)
    }

    override fun onAttach(context: Context) {
        context.activityComponent.fragmentComponent().build().also {
            it.inject(this)
        }
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity()

        binding = FragmentTopBinding.inflate(
            inflater, container, false
        )
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->

            viewModel.loading = true
            rankAdapter.addLoading()

            viewModel.ranksLiveData.observe(viewLifecycleOwner) { ranks ->
                ranks?.let {
                    rankAdapter.submitList(ranks)
                    viewModel.loading = false

                } ?: (context as MainActivity).showConnectionError {
                    viewModel.loadTopRanks(
                        null,
                        null,
                        context
                    )
                }
            }

            initSeasonSpinner(context, binding.season) { seasonName, context ->
                changeSeason(seasonName, context)
            }

        }

        initRecyclerVIew()

        setHeaderField(binding.head.rankNumber, "rank")
        setHeaderField(binding.head.nickname, "nickname")
        setHeaderField(binding.head.mmr, "mmr")
    }

    private fun changeSeason(seasonName: String, context: Context) {
        viewModel.loading = true
        rankAdapter.submitList(null)
        rankAdapter.addLoading()
        viewModel.changeSeason(seasonName, context)
    }

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

        viewModel.getCurrentSeason(context)?.let {
            spinner.setSelection((spinner.adapter as ArrayAdapter<String>).getPosition(it.title))
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!viewModel.loading) {
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

    private fun setHeaderField(textVIew: TextView, text: String) {
        textVIew.text = text
        textVIew.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        textVIew.setTypeface(null, Typeface.BOLD)
    }

    private fun initRecyclerVIew() {

        binding.recyclerViewRanks.adapter = rankAdapter

        binding.recyclerViewRanks.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !viewModel.loading) //check for scroll down
                {
                    val visibleItemCount =
                        recyclerView.layoutManager?.childCount ?: 0
                    val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                    val firstVisibleItems =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItems) >= totalItemCount
                        && firstVisibleItems >= 0
                    ) {
                        viewModel.loading = true
                        rankAdapter.addLoading()
                        viewModel.loadList(totalItemCount, totalItemCount + 20)
                    }
                }
            }
        })

    }

}