package com.skash.timetrack.core.model

import java.util.Date

data class TaskGroup(
    val date: Date,
    val tasks: List<TaskSection>
) {
    val time by lazy {
        tasks.sumOf { it.time }
    }
}

data class TaskSection(
    val date: Date,
    val tasks: List<Task>,
    val project: Project?,
    val description: String
) {
    val time by lazy {
        tasks.sumOf { it.duration }
    }
}