package com.skash.timetrack.feature.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.skash.timetrack.feature.service.ProjectTimerService

class ElapsedTimeBroadcastReceiver(
    private val onTimeElapsed: (Int) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }

        if (intent.action != ProjectTimerService.TIMER_TICK) {
            return
        }

        onTimeElapsed(intent.getIntExtra(ProjectTimerService.TIME_ELAPSED, 0))
    }
}