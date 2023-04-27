package com.skash.timetrack.core.model

import com.skash.timetrack.api.network.model.TaskResponse
import java.util.Date
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val project: Project?,
    val description: String,
    val startedAt: Date,
    val endedAt: Date,
    val duration: Int
) {

    constructor(apiModel: TaskResponse) : this(
        id = apiModel.id ?: UUID.randomUUID(),
        project = Project(apiModel.project!!),
        description = apiModel.description ?: "",
        startedAt = apiModel.startedAt ?: Date(),
        endedAt = apiModel.endedAt ?: Date(),
        duration = apiModel.duration ?: 0
    )
}