package com.skash.timetrack.feature.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.skash.timetrack.feature.service.ReloadService

class ReloadBroadcastReceiver(
    private val onDataReloaded: () -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }

        if (intent.action != ReloadService.RELOADED_ACTION) {
            return
        }

        onDataReloaded()
    }
}