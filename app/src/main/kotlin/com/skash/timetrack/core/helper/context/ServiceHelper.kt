package com.skash.timetrack.core.helper.context

import android.content.Context
import android.content.Intent
import com.skash.timetrack.feature.service.ProjectTimerService

fun Context.getTaskTimerStatus() {
    val projectService = Intent(
        this,
        ProjectTimerService::class.java
    )
    projectService.putExtra(
        ProjectTimerService.TIMER_ACTION,
        ProjectTimerService.GET_STATUS
    )

    startService(projectService)
}

fun Context.startTaskTimer() {
    val projectService = Intent(
        this,
        ProjectTimerService::class.java
    )
    projectService.putExtra(
        ProjectTimerService.TIMER_ACTION,
        ProjectTimerService.START
    )

    startService(projectService)
}

fun Context.stopTaskTimer() {
    val projectService = Intent(
        this,
        ProjectTimerService::class.java
    )
    projectService.putExtra(
        ProjectTimerService.TIMER_ACTION,
        ProjectTimerService.STOP
    )

    startService(projectService)
}

fun Context.moveTaskTimerToForeground() {
    val projectService = Intent(this, ProjectTimerService::class.java)
    projectService.putExtra(
        ProjectTimerService.TIMER_ACTION,
        ProjectTimerService.MOVE_TO_FOREGROUND
    )
    startService(projectService)
}

fun Context.moveTaskTimerToBackground() {
    val projectService = Intent(
        this,
        ProjectTimerService::class.java
    )
    projectService.putExtra(
        ProjectTimerService.TIMER_ACTION,
        ProjectTimerService.MOVE_TO_BACKGROUND
    )

    startService(projectService)
}