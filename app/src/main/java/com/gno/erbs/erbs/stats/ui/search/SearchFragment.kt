package com.gno.erbs.erbs.stats.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.MainActivity
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentSearchBinding
import com.gno.erbs.erbs.stats.model.FoundObjectsTypes
import com.gno.erbs.erbs.stats.repository.NavigateHelper
import com.gno.erbs.erbs.stats.ui.base.BaseFragment

class SearchFragment : BaseFragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private val searchAdapter = SearchAdapter { foundObject ->

        val bundle = bundleOf(
            "code" to foundObject.code,
            "name" to foundObject.name,
            "seasonId" to "0"
        )

        when (foundObject.type) {
            FoundObjectsTypes.PLAYER -> NavigateHelper.go(
                findNavController(),
                R.id.nav_user_stats,
                bundle
            )
            FoundObjectsTypes.CHARACTER -> NavigateHelper.go(
                findNavController(),
                R.id.nav_character_detail,
                bundle
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.find(arguments?.getString("searchString", "") ?: "")
        searchAdapter.addLoading()

        binding.recyclerviewMenu.adapter = searchAdapter


        viewModel.foundObjectLiveData.observe(viewLifecycleOwner) { foundObjects ->
            foundObjects.forEach {
                if (it == null) (activity as MainActivity).showConnectionError { searchAdapter.addLoading() }
            }
            searchAdapter.submitList(foundObjects.filterNotNull())
        }
    }

}