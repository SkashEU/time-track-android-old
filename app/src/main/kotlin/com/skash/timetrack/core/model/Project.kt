package com.skash.timetrack.core.model

import android.os.Parcelable
import com.skash.timetrack.api.network.model.ProjectResponse
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

    override fun toString(): String {
        return title
    }

}
