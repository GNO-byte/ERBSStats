package com.gno.erbs.erbs.stats.ui.guide.characters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentCharactersBinding
import com.gno.erbs.erbs.stats.repository.NavigateHelper
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import com.gno.erbs.erbs.stats.ui.home.HomeViewModel
import javax.inject.Inject

class CharactersFragment : BaseFragment() {

    private lateinit var binding: FragmentCharactersBinding

    @Inject
    lateinit var navigateHelper: NavigateHelper

    @Inject
    lateinit var factory: CharactersViewModel.CharactersViewModelFactory
    private val viewModel: CharactersViewModel by viewModels { factory }

    @Inject
    lateinit var characterGuidesAdapter: CharactersAdapter

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

        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewCharacters.adapter = characterGuidesAdapter
        characterGuidesAdapter.addLoading()

        viewModel.charactersLiveData.observe(viewLifecycleOwner) { characters ->
            characters?.let {
                characterGuidesAdapter.submitList(characters)
            } ?: activity?.let {
                (it as MainActivity).showConnectionError { viewModel.loadCharacters() }
            }
        }
    }
}