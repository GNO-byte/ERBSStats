package com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentWeaponTypesBinding
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.CharacterDetailViewModel


class WeaponTypesFragment : Fragment() {

    companion object {
        fun newInstance() = WeaponTypesFragment()
    }

    private lateinit var binding: FragmentWeaponTypesBinding
    private lateinit var viewModel: CharacterDetailViewModel
    private val weaponTypesAdapter = WeaponTypesAdapter()

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

        activity?.let { thisActivity ->
            weaponTypesAdapter.addLoading()
            viewModel =
                ViewModelProvider(
                    thisActivity.supportFragmentManager.fragments.first()
                        .childFragmentManager.fragments[0]
                ).get(
                    CharacterDetailViewModel::class.java
                )
        }
        binding.recyclerviewWeaponTypes.adapter = weaponTypesAdapter
        viewModel.resultWeaponTypesLiveData.observe(viewLifecycleOwner) { weaponTypes ->
            weaponTypesAdapter.submitList(weaponTypes)
        }
    }
}

