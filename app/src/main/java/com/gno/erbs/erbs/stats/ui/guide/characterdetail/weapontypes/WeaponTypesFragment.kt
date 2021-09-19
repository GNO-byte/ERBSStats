package com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gno.erbs.erbs.stats.databinding.FragmentWeaponTypesBinding
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.CharacterDetailViewModel
import javax.inject.Inject


class WeaponTypesFragment : Fragment() {

    companion object {
        fun newInstance() = WeaponTypesFragment()
    }

    private lateinit var binding: FragmentWeaponTypesBinding
    private lateinit var viewModel: CharacterDetailViewModel

    @Inject
    lateinit var weaponTypesAdapter : WeaponTypesAdapter

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
        binding = FragmentWeaponTypesBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let { activity ->
            weaponTypesAdapter.addLoading()
            viewModel =
                ViewModelProvider(
                    activity.supportFragmentManager.fragments.first()
                        .childFragmentManager.fragments[0]
                ).get(
                    CharacterDetailViewModel::class.java
                )
        }
        binding.recyclerviewWeaponTypes.adapter = weaponTypesAdapter
        viewModel.resultWeaponTypesLiveData.observe(viewLifecycleOwner) { weaponTypes ->
            weaponTypes?.let {
                weaponTypesAdapter.submitList(weaponTypes)
            }
        }
    }
}

