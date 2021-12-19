package com.listocalixto.android.ecommerce.util

import android.text.TextUtils
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.ecommerce.R

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun showSnackbar(
    view: View,
    @StringRes snackbarText: Int,
    timeLength: Int,
    anchorView: View?,
    isAnError: Boolean
) {
    Snackbar.make(view, snackbarText, timeLength).run {
        if (isAnError) {
            setBackgroundTint(MaterialColors.getColor(view, R.attr.colorError))
            setTextColor(MaterialColors.getColor(view, R.attr.colorOnError))
            setActionTextColor(MaterialColors.getColor(view, R.attr.colorOnError))
        }
        setAnchorView(anchorView)
        show()
    }
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}