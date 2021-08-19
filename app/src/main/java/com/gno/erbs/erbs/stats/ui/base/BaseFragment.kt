package com.gno.erbs.erbs.stats.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.RequestListener
import com.facebook.shimmer.ShimmerDrawable
import com.gno.erbs.erbs.stats.MainActivity
import com.gno.erbs.erbs.stats.ui.LoadingImageHelper

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).searchEnable()
    }

    protected fun createGlideListener(
        image: ImageView,
        loading: View
    ): RequestListener<Drawable> = LoadingImageHelper.createGlideListener(image, loading)


    fun loadImage(
        view: ImageView, webLink: String?,
        loading: View
    ) {
        LoadingImageHelper.loadImage(view, webLink, loading)
    }

    protected fun createShimmer(context: Context): ShimmerDrawable {
        return LoadingImageHelper.createShimmer(context)
    }

}