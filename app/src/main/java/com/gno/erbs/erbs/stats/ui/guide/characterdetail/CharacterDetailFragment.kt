package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentCharacterDetailBinding
import com.gno.erbs.erbs.stats.ui.base.BaseFragment

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

        binding.name.visibility = View.GONE
        binding.loadingName.visibility = View.VISIBLE

        binding.characterImage.visibility = View.GONE
        binding.loadingImage.visibility = View.VISIBLE


        viewModel.loadCharacterDetail(code)

        activity?.let { thisActivity ->
            binding.pager.adapter =
                CharacterDetailViewPagerAdapter(
                    thisActivity.supportFragmentManager
                )
            binding.tabs.setupWithViewPager(binding.pager)
            viewModel.characterStatsLiveData.observe(viewLifecycleOwner) { characterStats ->
                binding.name.text = characterStats.name

                binding.loadingName.visibility = View.GONE
                binding.name.visibility = View.VISIBLE

                context?.let { thisContext ->
                    Glide.with(thisContext)
                        .load(characterStats.characterImageHalfWebLink ?: R.drawable.loading_image)
                        .placeholder(createShimmer(thisContext))
                        .error(R.drawable.loading_image)
                        .into(binding.characterImage)

                    binding.loadingImage.visibility = View.GONE
                    binding.characterImage.visibility = View.VISIBLE
                }

            }
        }
    }
}