package com.gno.erbs.erbs.stats.ui.guide.characterdetail.background

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.databinding.FragmentBackgroundBinding
import com.gno.erbs.erbs.stats.ui.activityComponent
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

        binding.background.visibility = View.GONE
        binding.loading.visibility = View.VISIBLE

        activity?.let { activity ->
            viewModel =
                ViewModelProvider(
                    activity.supportFragmentManager.fragments.first()
                        .childFragmentManager.fragments[0]
                ).get(
                    CharacterDetailViewModel::class.java
                )
        }

        viewModel.coreCharacterLiveData.observe(viewLifecycleOwner) { coreCharacter ->
            coreCharacter?.let {
                binding.background.text = coreCharacter.background ?: ""
                binding.loading.visibility = View.GONE
                binding.background.visibility = View.VISIBLE
            }
        }
    }
}