package com.skash.timetrack.core.helper.context

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.skash.timetrack.feature.service.ProjectTimeForegroundService

fun Context.getProjectTimerStatus() {
    val stopwatchService = Intent(
        this,
        ProjectTimeForegroundService::class.java
    )
    stopwatchService.putExtra(
        ProjectTimeForegroundService.TIMER_ACTION,
        ProjectTimeForegroundService.GET_STATUS
    )

    startService(stopwatchService)
}

fun Context.startProjectTimer() {
    val stopwatchService = Intent(
        this,
        ProjectTimeForegroundService::class.java
    )
    stopwatchService.putExtra(
        ProjectTimeForegroundService.TIMER_ACTION,
        ProjectTimeForegroundService.START
    )

    startService(stopwatchService)
}

fun Context.pauseProjectTimer() {
    val stopwatchService = Intent(
        this,
        ProjectTimeForegroundService::class.java
    )
    stopwatchService.putExtra(
        ProjectTimeForegroundService.TIMER_ACTION,
        ProjectTimeForegroundService.STOP
    )

    startService(stopwatchService)
}

fun Context.moveProjectTimerToForeground() {
    val stopwatchService = Intent(this, ProjectTimeForegroundService::class.java)
    stopwatchService.putExtra(
        ProjectTimeForegroundService.TIMER_ACTION,
        ProjectTimeForegroundService.MOVE_TO_FOREGROUND
    )
    startService(stopwatchService)
}

fun Context.moveProjectTimerToBackground() {
    val stopwatchService = Intent(
        this,
        ProjectTimeForegroundService::class.java
    )
    stopwatchService.putExtra(
        ProjectTimeForegroundService.TIMER_ACTION,
        ProjectTimeForegroundService.MOVE_TO_BACKGROUND
    )

    startService(stopwatchService)
}