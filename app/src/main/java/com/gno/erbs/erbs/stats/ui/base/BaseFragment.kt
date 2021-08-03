package com.gno.erbs.erbs.stats.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.gno.erbs.erbs.stats.MainActivity
import com.gno.erbs.erbs.stats.R
import com.google.android.material.color.MaterialColors

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).searchEnable()
    }

    protected fun createShimmer(context: Context): ShimmerDrawable {
        return ShimmerDrawable().apply {
            val shimmer =
                Shimmer.ColorHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                    .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                    .setBaseAlpha(0.7f) //the alpha of the underlying children
                    .setHighlightAlpha(0.6f) // the shimmer alpha amount
                    .setBaseColor(
                        MaterialColors.getColor(
                            context,
                            R.attr.colorSecondary,
                            ContextCompat.getColor(context, R.color.goldenrod)
                        )
                    )
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .build()

            setShimmer(shimmer)
        }
    }
}