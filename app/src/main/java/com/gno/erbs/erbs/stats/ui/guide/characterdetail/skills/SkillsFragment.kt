package com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.databinding.FragmentSkillsBinding
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.CharacterDetailViewModel
import javax.inject.Inject

class SkillsFragment : Fragment() {

    companion object {
        fun newInstance() = SkillsFragment()
    }

    private lateinit var binding: FragmentSkillsBinding
    private lateinit var viewModel: CharacterDetailViewModel

    @Inject
    lateinit var skillsAdapter: SkillsAdapter

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
        binding = FragmentSkillsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let { activity ->

            skillsAdapter.addLoading()

            viewModel =
                ViewModelProvider(
                    activity.supportFragmentManager.fragments.first()
                        .childFragmentManager.fragments[0]
                ).get(
                    CharacterDetailViewModel::class.java
                )
        }
        binding.recyclerviewWeaponSkills.adapter = skillsAdapter
        viewModel.skillsLiveData.observe(viewLifecycleOwner) { coreSkills ->
            coreSkills?.let {
                skillsAdapter.submitList(coreSkills)
            }
        }
    }


}