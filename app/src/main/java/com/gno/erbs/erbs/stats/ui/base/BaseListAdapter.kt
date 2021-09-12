package com.gno.erbs.erbs.stats.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestListener
import com.facebook.shimmer.ShimmerDrawable
import com.gno.erbs.erbs.stats.ui.LoadingImageHelper

abstract class BaseListAdapter<T, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, VH>(diffCallback) {

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