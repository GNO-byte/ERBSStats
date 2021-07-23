package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment : Fragment() {

    private val viewModel: CharacterDetailViewModel by lazy {
        ViewModelProvider(this).get(CharacterDetailViewModel::class.java)
    }

    private lateinit var binding: FragmentCharacterDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCharacterDetailBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val code = arguments?.getInt("code", 1) ?: 1
        viewModel.loadCharacterDetail(code)

        activity?.let { thisActivity ->
            binding.pager.adapter =
                CharacterDetailViewPagerAdapter(
                    thisActivity.supportFragmentManager
                )
            binding.tabs.setupWithViewPager(binding.pager)
            viewModel.characterStatsLiveData.observe(viewLifecycleOwner) { characterStats ->
                binding.name.text = characterStats.name
                characterStats.characterImageHalfWebLink?.let { characterImageHalfWebLink ->
                    context?.let { thisContext ->
                        Glide.with(thisContext).load(characterImageHalfWebLink)
                            .into(binding.characterImage)
                    }
                }
            }
        }
    }
}