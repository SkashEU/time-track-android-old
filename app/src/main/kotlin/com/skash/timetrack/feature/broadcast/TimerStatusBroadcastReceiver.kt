package com.skash.timetrack.feature.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.skash.timetrack.core.model.TimerStatus
import com.skash.timetrack.feature.service.TaskTimerService
import com.skash.timetrack.feature.service.TimerService

class TimerStatusBroadcastReceiver(
    private val onStateChanged: (TimerStatus) -> Unit = {}
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }

        val status = TimerStatus(
            intent.getBooleanExtra(TimerService.IS_TIMER_RUNNING, false),
            intent.getIntExtra(TimerService.TIME_ELAPSED, 0),
            intent.getBooleanExtra(TimerService.IS_TIMER_FINISHED, false)
        )

        onStateChanged(status)
    }
}