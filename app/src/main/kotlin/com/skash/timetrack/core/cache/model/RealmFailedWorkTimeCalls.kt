package com.skash.timetrack.core.cache.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.util.Date

open class RealmFailedWorkTimeCalls(
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),
    var startedAt: Date = Date(),
    var endedAt: Date = Date(),
    var duration: Int = 0
) : RealmObject()