package com.skash.timetrack.core.model

import java.util.Date

data class TaskGroup(
    val date: Date,
    val tasks: List<Task>
)