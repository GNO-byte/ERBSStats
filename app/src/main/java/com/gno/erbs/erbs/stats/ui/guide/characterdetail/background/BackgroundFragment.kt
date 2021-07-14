package com.gno.erbs.erbs.stats.ui.guide.characterdetail.background

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentBackgroundBinding
import com.gno.erbs.erbs.stats.databinding.FragmentCharacterStatsBinding
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.CharacterDetailViewModel

class BackgroundFragment : Fragment() {

    companion object {
        fun newInstance() = BackgroundFragment()
    }

    private lateinit var binding: FragmentBackgroundBinding
    private lateinit var viewModel: CharacterDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBackgroundBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let { thisActivity ->
            viewModel =
                ViewModelProvider(thisActivity.supportFragmentManager.fragments.first()
                    .childFragmentManager.fragments[0]).get(
                    CharacterDetailViewModel::class.java
                )
        }

        viewModel.coreCharacterLiveData.observe(viewLifecycleOwner) {
            binding.background.text = it?.background ?:""
        }
    }
}