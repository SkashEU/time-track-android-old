package com.skash.timetrack.core.helper.context

import android.content.Context
import android.content.Intent
import com.skash.timetrack.feature.service.TaskTimerService
import com.skash.timetrack.feature.service.TimerService
import com.skash.timetrack.feature.service.WorkTimeTimerService

fun Context.getTaskTimerStatus() {
    val service = Intent(
        this,
        TaskTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.GET_STATUS
    )

    startService(service)
}

fun Context.getWorkTimerStatus() {
    val service = Intent(
        this,
        WorkTimeTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.GET_STATUS
    )

    startService(service)
}

fun Context.startTaskTimer() {
    val service = Intent(
        this,
        TaskTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.START
    )

    startService(service)
}

fun Context.startWorkTimeTimer() {
    val service = Intent(
        this,
        WorkTimeTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.START
    )

    startService(service)
}

fun Context.stopTaskTimer() {
    val service = Intent(
        this,
        TaskTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.STOP
    )

    startService(service)
}

fun Context.stopWorkTimeTimer() {
    val service = Intent(
        this,
        WorkTimeTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.STOP
    )

    startService(service)
}

fun Context.moveTaskTimerToForeground() {
    val service = Intent(
        this,
        TaskTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.MOVE_TO_FOREGROUND
    )
    startService(service)
}

fun Context.moveWorkTimeTimerToForeground() {
    val service = Intent(
        this,
        WorkTimeTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.MOVE_TO_FOREGROUND
    )
    startService(service)
}

fun Context.moveTaskTimerToBackground() {
    val service = Intent(
        this,
        TaskTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.MOVE_TO_BACKGROUND
    )

    startService(service)
}

fun Context.moveWorkTimeTimerToBackground() {
    val service = Intent(
        this,
        WorkTimeTimerService::class.java
    )
    service.putExtra(
        TimerService.TIMER_ACTION,
        TimerService.MOVE_TO_BACKGROUND
    )

    startService(service)
}