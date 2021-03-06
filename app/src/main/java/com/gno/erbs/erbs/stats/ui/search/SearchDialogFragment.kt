package com.gno.erbs.erbs.stats.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.databinding.FragmentSearchDialogBinding
import com.gno.erbs.erbs.stats.repository.NavigateHelper
import com.gno.erbs.erbs.stats.ui.activityComponent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject


class SearchDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var navigateHelper: NavigateHelper

    companion object {
        fun newInstance(changeColor: Boolean = false) = SearchDialogFragment().apply {
            arguments = bundleOf(
                "CHANGE_COLOR" to changeColor
            )
        }
    }

    lateinit var binding: FragmentSearchDialogBinding

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
        binding = FragmentSearchDialogBinding.inflate(inflater, container, false)

        val changeColor = arguments?.getBoolean("CHANGE_COLOR", false) ?: false
        if (changeColor) {
            context?.let { context ->
                binding.group.background =
                    ContextCompat.getDrawable(context, R.drawable.rounded_search_border)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val bundle = bundleOf("searchString" to query)
                navigateHelper.go(findNavController(), R.id.nav_search, bundle)
                dismiss()
                return false
            }
        })

    }

}