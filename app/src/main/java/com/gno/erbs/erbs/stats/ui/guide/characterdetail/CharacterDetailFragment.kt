package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gno.erbs.erbs.stats.databinding.FragmentCharacterDetailBinding
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class CharacterDetailFragment : BaseFragment() {

    val code by lazy { arguments?.getInt("code", 1) ?: 1 }
    val name by lazy { arguments?.getString("name", "") }

    @Inject
    lateinit var factory: CharacterDetailViewModel.CharacterDetailViewModelFactory.Factory
    private val viewModel: CharacterDetailViewModel by viewModels {
        factory.create(code)
    }

    private lateinit var binding: FragmentCharacterDetailBinding

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

        binding = FragmentCharacterDetailBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.text = name

        binding.characterImage.visibility = View.GONE
        binding.loadingImage.visibility = View.VISIBLE


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
                    viewModel.loadCharacterDetail()
                }
            }
        }
    }
}