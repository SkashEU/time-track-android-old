package com.skash.timetrack.core.helper.context

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skash.timetrack.R
import com.skash.timetrack.core.helper.state.ErrorType

fun Context.showErrorAlert(errorType: ErrorType) {
    MaterialAlertDialogBuilder(this)
        .setMessage(errorType.errorMessage)
        .setPositiveButton(R.string.alert_ok_button_title, null)
        .show()
}