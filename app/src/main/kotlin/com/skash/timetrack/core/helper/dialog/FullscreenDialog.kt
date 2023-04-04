package com.skash.timetrack.core.helper.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.skash.timetrack.R


open class FullscreenDialogFragment(
    @LayoutRes contentLayoutId: Int
) : DialogFragment(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}