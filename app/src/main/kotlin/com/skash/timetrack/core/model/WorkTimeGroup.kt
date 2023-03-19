package com.skash.timetrack.core.model

import java.util.Date

data class WorkTimeGroup(
    val date: Date,
    val workTimes: List<WorkTime>
)