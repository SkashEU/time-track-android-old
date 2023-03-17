package com.skash.timetrack.core.cache.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class RealmProjectColor(
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),
    var hex: String = ""
) : RealmObject()