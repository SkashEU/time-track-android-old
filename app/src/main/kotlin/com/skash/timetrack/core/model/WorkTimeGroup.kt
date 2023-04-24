package com.skash.timetrack.core.model

import java.util.Date

data class WorkTimeGroup(
    val date: Date,
    val workTimes: List<WorkTimeSection>
)

data class WorkTimeSection(
    val date: Date,
    val workTimes: List<WorkTime>
) {
    val time by lazy {
        workTimes.sumOf { it.duration }
    }
}