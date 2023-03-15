package com.skash.timetrack.core.cache.model

import com.skash.timetrack.core.model.ProjectTime
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.util.Date

open class RealmProjectTime(
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),
    var project: RealmProject? = null,
    var description: String = "",
    var startedAt: Date = Date(),
    var endedAt: Date = Date(),
    var duration: Int = 0
) : RealmObject() {

    constructor(model: ProjectTime) : this(
        project = model.project?.let { RealmProject(it) },
        description = model.description,
        startedAt = model.startedAt,
        endedAt = model.endedAt,
        duration = model.duration
    )
}