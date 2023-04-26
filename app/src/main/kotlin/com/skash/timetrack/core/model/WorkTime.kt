package com.skash.timetrack.core.model

import com.skash.timetrack.api.network.model.WorktimeResponse
import java.util.Date
import java.util.UUID

data class WorkTime(
    val id: UUID = UUID.randomUUID(),
    val startedAt: Date,
    val endedAt: Date,
    val duration: Int
) {

    constructor(apiModel: WorktimeResponse) : this(
        id = apiModel.id ?: UUID.randomUUID(),
        startedAt = apiModel.startedAt ?: Date(),
        endedAt = apiModel.endedAt ?: Date(),
        duration = apiModel.duration ?: 0
    )
}