package com.gno.erbs.erbs.stats.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentHomeBinding
import com.readystatesoftware.systembartint.SystemBarTintManager


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding

    private val menuAdapter = HomeAdapter {
        findNavController().navigate(it)
    }

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

        binding.recyclerViewMenu.adapter = menuAdapter

        viewModel.menuObjectLiveData.observe(viewLifecycleOwner) { menu ->
            menuAdapter.submitList(menu)
           // binding.searchGroup.
        }

    }

//    private fun initStatusBar(color: Int) {
//        activity?.let { thisActivity ->
//            val window = thisActivity.window
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                val tintManager = SystemBarTintManager(thisActivity)
//                tintManager.isStatusBarTintEnabled = true
//                tintManager.setTintColor(color)
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.decorView.systemUiVisibility =
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                window.statusBarColor = color
//            }
//        }
//    }
}

