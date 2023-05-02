package com.skash.timetrack.core.model

import com.skash.timetrack.api.network.model.TaskResponse
import com.skash.timetrack.core.cache.model.RealmFailedTaskCalls
import com.skash.timetrack.core.cache.model.RealmTask
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

    constructor(realmModel: RealmTask) : this(
        id = realmModel.serverId,
        project = realmModel.project?.let { Project(it) },
        description = realmModel.description,
        startedAt = realmModel.startedAt,
        endedAt = realmModel.endedAt,
        duration = realmModel.duration
    )

    constructor(failModel: RealmFailedTaskCalls) : this(
        project = failModel.project?.let { Project(it) },
        description = failModel.description,
        startedAt = failModel.startedAt,
        endedAt = failModel.endedAt,
        duration = failModel.duration
    )
}