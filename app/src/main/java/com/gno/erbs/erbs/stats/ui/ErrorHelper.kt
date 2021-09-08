package com.gno.erbs.erbs.stats.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar


object ErrorHelper {

    fun showConnectionError(view: View, function: () -> Unit) {

        Snackbar.make(
            view,
            "Connection error. Check your internet connection",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("RETRY") {
            function.invoke()
        }.show()

    }

}