package com.skash.timetrack.core.cache.model

import com.skash.timetrack.core.model.Project
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class RealmProject(
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),
    var serverId: Int = 0,
    var title: String = "",
    var color: String = ""
) : RealmObject() {

    constructor(model: Project) : this(
        serverId = model.id,
        title = model.title,
        color = model.color
    )
}