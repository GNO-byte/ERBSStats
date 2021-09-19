package com.gno.erbs.erbs.stats.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.RequestListener
import com.facebook.shimmer.ShimmerDrawable
import com.gno.erbs.erbs.stats.di.component.FragmentComponent
import com.gno.erbs.erbs.stats.ui.MainActivity
import com.gno.erbs.erbs.stats.ui.LoadingImageHelper
import com.gno.erbs.erbs.stats.ui.activityComponent
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    lateinit var fragmentComponent: FragmentComponent

    @Inject
    lateinit var loadingImageHelper: LoadingImageHelper


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).searchEnable()
    }

    override fun onAttach(context: Context) {
        context.activityComponent.fragmentComponent().fragment(this).build().also {
            it.inject(this)
        }
        super.onAttach(context)
    }

    protected fun createGlideListener(
        image: ImageView,
        loading: View
    ): RequestListener<Drawable> = loadingImageHelper.createGlideListener(image, loading)

    fun loadImage(
        view: ImageView, webLink: String?,
        loading: View
    ) {
        loadingImageHelper.loadImage(view, webLink, loading)
    }

    protected fun createShimmer(context: Context): ShimmerDrawable {
        return loadingImageHelper.createShimmer(context)
    }

}