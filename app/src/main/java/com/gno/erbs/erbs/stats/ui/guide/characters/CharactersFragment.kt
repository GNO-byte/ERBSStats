package com.gno.erbs.erbs.stats.ui.guide.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.MainActivity
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentCharactersBinding
import com.gno.erbs.erbs.stats.ui.base.BaseFragment

class CharactersFragment : BaseFragment() {

    private lateinit var binding: FragmentCharactersBinding

    private val viewModel: CharactersViewModel by lazy {
        ViewModelProvider(this).get(CharactersViewModel::class.java)
    }

    private val characterGuidesAdapter = CharactersAdapter { code ->
        val bundle = bundleOf("code" to code)
        findNavController().navigate(R.id.nav_character_detail, bundle)
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
        viewModel.charactersLiveData.observe(viewLifecycleOwner) {
            characterGuidesAdapter.submitList(it)
        }
    }
}