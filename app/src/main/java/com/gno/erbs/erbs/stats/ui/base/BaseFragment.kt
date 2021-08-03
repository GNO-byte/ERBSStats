package com.gno.erbs.erbs.stats.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gno.erbs.erbs.stats.MainActivity

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).searchEnable()
    }
}