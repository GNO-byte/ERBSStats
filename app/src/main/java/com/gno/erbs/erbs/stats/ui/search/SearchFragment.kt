package com.gno.erbs.erbs.stats.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentSearchBinding
import com.gno.erbs.erbs.stats.model.FoundObjectsTypes
import com.gno.erbs.erbs.stats.repository.NavigateHelper
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    @Inject
    lateinit var navigateHelper: NavigateHelper

    companion object {
        fun newInstance() = SearchFragment()
    }

    lateinit var binding: FragmentSearchBinding
    private val searchString: String by lazy {
        arguments?.getString("searchString", "") ?: ""
    }

    @Inject
    lateinit var factory: SearchViewModel.SearchViewModelFactory.Factory
    private val viewModel: SearchViewModel by viewModels {
        factory.create(searchString)
    }

    private val searchAdapter = SearchAdapter { foundObject ->

        val bundle = bundleOf(
            "code" to foundObject.code,
            "name" to foundObject.name,
            "seasonId" to "0"
        )

        when (foundObject.type) {
            FoundObjectsTypes.PLAYER -> navigateHelper.go(
                findNavController(),
                R.id.nav_user_stats,
                bundle
            )
            FoundObjectsTypes.CHARACTER -> navigateHelper.go(
                findNavController(),
                R.id.nav_character_detail,
                bundle
            )
        }
    }

    override fun onAttach(context: Context) {
        context.activityComponent.fragmentComponent().fragment(this).build().also {
            it.inject(this)
        }
        super.onAttach(context)
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