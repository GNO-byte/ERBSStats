package com.gno.erbs.erbs.stats.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHelper @Inject constructor() {

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