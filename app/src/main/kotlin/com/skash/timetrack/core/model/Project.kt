package com.skash.timetrack.core.model

import android.os.Parcelable
import com.skash.timetrack.api.network.model.ProjectResponse
import com.skash.timetrack.core.cache.model.RealmClient
import com.skash.timetrack.core.cache.model.RealmProject
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Project(
    val id: UUID,
    val title: String,
    val color: String,
    val client: Client
) : Parcelable {

    constructor(apiModel: ProjectResponse) : this(
        id = apiModel.id ?: UUID.randomUUID(),
        title = apiModel.title ?: "",
        color = apiModel.color ?: "",
        client = Client(apiModel.client!!)
    )

    constructor(realmModel: RealmProject) : this(
        id = realmModel.serverId,
        title = realmModel.title,
        color = realmModel.color,
        client = Client(realmModel.client ?: RealmClient(title = ""))
    )

    override fun toString(): String {
        return title
    }

}
