package com.skash.timetrack.feature.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.skash.timetrack.feature.service.ProjectTimeForegroundService

class ElapsedTimeBroadcastReceiver(
    private val onTimeElapsed: (Int) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }

        if (intent.action != ProjectTimeForegroundService.TIMER_TICK) {
            return
        }

        onTimeElapsed(intent.getIntExtra(ProjectTimeForegroundService.TIME_ELAPSED, 0))
    }
}