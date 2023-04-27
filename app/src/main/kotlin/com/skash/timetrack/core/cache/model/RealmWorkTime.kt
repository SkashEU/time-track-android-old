package com.skash.timetrack.core.cache.model

import com.skash.timetrack.core.model.WorkTime
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.util.Date
import java.util.UUID

open class RealmWorkTime(
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),
    var serverId: UUID = UUID.randomUUID(),
    var startedAt: Date = Date(),
    var endedAt: Date = Date(),
    var duration: Int = 0
) : RealmObject() {

    constructor(workTime: WorkTime) : this(
        serverId = workTime.id,
        startedAt = workTime.startedAt,
        endedAt = workTime.endedAt,
        duration = workTime.duration
    )
}