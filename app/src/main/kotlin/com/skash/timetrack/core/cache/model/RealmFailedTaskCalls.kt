package com.skash.timetrack.core.cache.model

import com.skash.timetrack.core.model.Task
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.util.Date

open class RealmFailedTaskCalls(
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),
    var project: RealmProject? = null,
    var description: String = "",
    var startedAt: Date = Date(),
    var endedAt: Date = Date(),
    var duration: Int = 0
) : RealmObject() {

    constructor(task: Task) : this(
        project = task.project?.let { RealmProject(it) },
        description = task.description,
        startedAt = task.startedAt,
        endedAt = task.endedAt,
        duration = task.duration
    )

}