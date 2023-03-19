package com.skash.timetrack.core.cache.model

import com.skash.timetrack.core.model.Task
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.util.Date

open class RealmTask(
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),
    var project: RealmProject? = null,
    var description: String = "",
    var startedAt: Date = Date(),
    var endedAt: Date = Date(),
    var duration: Int = 0
) : RealmObject() {

    constructor(model: Task) : this(
        project = model.project?.let { RealmProject(it) },
        description = model.description,
        startedAt = model.startedAt,
        endedAt = model.endedAt,
        duration = model.duration
    )
}