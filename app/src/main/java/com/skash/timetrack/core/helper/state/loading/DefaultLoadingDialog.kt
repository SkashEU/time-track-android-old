package com.skash.timetrack.core.helper.state.loading

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.skash.timetrack.R


class DefaultLoadingDialog(context: Context) : LoadingDialog {
    private val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
            .setView(R.layout.dialog_loading)
            .setCancelable(false)

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun show() {
        if (dialog.isShowing) return
        dialog.show()
    }


    override fun dismiss() {
        if (!dialog.isShowing) return
        dialog.dismiss()
    }
}