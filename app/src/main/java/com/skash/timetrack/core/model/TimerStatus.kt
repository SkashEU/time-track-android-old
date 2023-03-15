package com.skash.timetrack.core.model

data class TimerStatus(
    val isTimerRunning: Boolean,
    val elapsedTime: Int,
    val finishedTime: Int? = null
)