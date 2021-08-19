package com.gno.erbs.erbs.stats.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.gno.erbs.erbs.stats.R
import com.google.android.material.color.MaterialColors
import com.google.android.material.imageview.ShapeableImageView

object LoadingImageHelper {


    fun loadImage(
        view: ImageView, webLink: String?,
        loading: View
    ) {
        Glide.with(view.context).load(webLink)
            .listener(
                createGlideListener(
                    view,
                    loading
                )
            )
            .error(R.drawable.loading_image)
            .into(view)
    }


    fun createShimmer(context: Context): ShimmerDrawable {
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


    fun createGlideListener(
        image: ImageView,
        loading: View
    ) = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {

            image.visibility = View.VISIBLE
            loading.visibility = View.GONE

            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: com.bumptech.glide.load.DataSource?,
            isFirstResource: Boolean
        ): Boolean {

            image.visibility = View.VISIBLE
            loading.visibility = View.GONE

            return false
        }
    }


}