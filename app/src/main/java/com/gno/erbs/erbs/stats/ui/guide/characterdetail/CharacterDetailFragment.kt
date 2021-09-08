package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.MainActivity
import com.gno.erbs.erbs.stats.databinding.FragmentCharacterDetailBinding
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class CharacterDetailFragment : BaseFragment() {

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
        super.onViewCreated(view, savedInstanceState)

        val code = arguments?.getInt("code", 1) ?: 1
        binding.name.text = arguments?.getString("name", "")

        binding.characterImage.visibility = View.GONE
        binding.loadingImage.visibility = View.VISIBLE

        viewModel.loadCharacterDetail(code)

        activity?.let { activity ->
            binding.pager.adapter =
                CharacterDetailViewPagerAdapter(
                    childFragmentManager,
                    lifecycle
                )

            TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
                tab.text =
                    (binding.pager.adapter as CharacterDetailViewPagerAdapter).getPageTitle(position)
            }.attach()

            viewModel.characterStatsLiveData.observe(viewLifecycleOwner) { characterStats ->
                characterStats?.let {
                    loadImage(
                        binding.characterImage,
                        characterStats.characterImageHalfWebLink,
                        binding.loadingImage
                    )
                } ?: (activity as MainActivity).showConnectionError {
                    viewModel.loadCharacterDetail(
                        code
                    )
                }
            }
        }
    }
}