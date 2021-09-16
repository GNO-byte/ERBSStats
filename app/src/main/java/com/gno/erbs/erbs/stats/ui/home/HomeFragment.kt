package com.gno.erbs.erbs.stats.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.databinding.FragmentHomeBinding
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import com.gno.erbs.erbs.stats.ui.home.adapter.HomeAdapter
import com.gno.erbs.erbs.stats.ui.home.slider.HomeSliderAdapter
import kotlin.math.abs


class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.recyclerViewMenu.adapter = HomeAdapter { navLink ->
            findNavController().navigate(navLink)
        }
        viewModel.menuObjectLiveData.observe(viewLifecycleOwner) { menu ->
            (binding.recyclerViewMenu.adapter as HomeAdapter).submitList(menu)
        }

        binding.slider.adapter = HomeSliderAdapter()
        viewModel.illustrationLiveData.observe(viewLifecycleOwner) { url ->
            (binding.slider.adapter as HomeSliderAdapter).submitList(url)
            initSliderAutoScroll()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).searchDisable()
    }

    private fun initSliderAutoScroll() {
        binding.slider.clipToPadding = false
        binding.slider.clipChildren = false
        binding.slider.offscreenPageLimit = 3
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.slider.setPageTransformer(compositePageTransformer)

        val sliderHandler = Handler()
        val sliderRunnable = Runnable {
            if ((binding.slider.adapter as HomeSliderAdapter).itemCount ==
                binding.slider.currentItem + 1
            ) binding.slider.currentItem = 0
            else binding.slider.currentItem = binding.slider.currentItem + 1
        }

        binding.slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000)
            }
        })
    }
}



