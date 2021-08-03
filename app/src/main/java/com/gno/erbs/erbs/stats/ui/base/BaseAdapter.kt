package com.gno.erbs.erbs.stats.ui.base

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.gno.erbs.erbs.stats.R
import com.google.android.material.color.MaterialColors

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, VH>(diffCallback) {

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