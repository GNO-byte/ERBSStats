package com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentSkillsBinding
import com.gno.erbs.erbs.stats.databinding.FragmentWeaponTypesBinding
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.CharacterDetailViewModel
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.characterstats.CharacterStatsFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes.WeaponTypesAdapter

class SkillsFragment : Fragment() {

    companion object {
        fun newInstance() = SkillsFragment()
    }

    private lateinit var binding: FragmentSkillsBinding
    private lateinit var viewModel: CharacterDetailViewModel
    private val skillsAdapter = SkillsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSkillsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let { thisActivity ->

            skillsAdapter.addLoading()

            viewModel =
                ViewModelProvider(
                    thisActivity.supportFragmentManager.fragments.first()
                        .childFragmentManager.fragments[0]
                ).get(
                    CharacterDetailViewModel::class.java
                )
        }
        binding.recyclerviewWeaponSkills.adapter = skillsAdapter
        viewModel.skillsLiveData.observe(viewLifecycleOwner) { coreSkills ->
            skillsAdapter.submitList(coreSkills)
        }
    }


}