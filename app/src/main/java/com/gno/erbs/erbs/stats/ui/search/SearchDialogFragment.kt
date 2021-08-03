package com.gno.erbs.erbs.stats.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.MainActivity
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentSearchDialogBinding

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors


class SearchDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(changeColor: Boolean = false) = SearchDialogFragment().apply {
            arguments = bundleOf(
                "CHANGE_COLOR" to changeColor
            )
        }
    }

    lateinit var binding: FragmentSearchDialogBinding
    private var searchView:SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchDialogBinding.inflate(inflater, container, false)

        val changeColor = arguments?.getBoolean("CHANGE_COLOR", false) ?: false
        if (changeColor) {
            context?.let { thisContext ->
                binding.group.background = ContextCompat.getDrawable(thisContext, R.drawable.rounded_search_border)
            }

        }

        if(searchView == null){
            searchView = binding.search
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val bundle = bundleOf("searchString" to query)
                findNavController().navigate(R.id.nav_search, bundle)
                dismiss()
                return false
            }
        })

    }

}