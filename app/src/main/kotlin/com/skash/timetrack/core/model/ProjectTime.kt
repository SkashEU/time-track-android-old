package com.skash.timetrack.core.model

import java.util.Date

data class ProjectTime(
    val id: Int = 0,
    val project: Project?,
    val description: String,
    val startedAt: Date,
    val endedAt: Date,
    val duration: Int
)