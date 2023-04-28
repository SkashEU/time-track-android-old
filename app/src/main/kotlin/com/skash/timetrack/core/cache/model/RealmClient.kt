package com.skash.timetrack.core.cache.model

import com.skash.timetrack.core.model.Client
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.util.UUID

open class RealmClient(
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),
    var serverId: UUID = UUID.randomUUID(),
    var title: String = "",
) : RealmObject() {

    constructor(model: Client) : this(
        serverId = model.id,
        title = model.title
    )
}