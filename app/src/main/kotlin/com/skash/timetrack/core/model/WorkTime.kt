package com.skash.timetrack.core.model

import java.util.Date

data class WorkTime(
    val id: Int = 0,
    val startedAt: Date,
    val endedAt: Date,
    val duration: Int
)