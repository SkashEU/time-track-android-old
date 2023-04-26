package com.skash.timetrack.core.model

import android.os.Parcelable
import com.skash.timetrack.api.network.model.WorkspaceResponse
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Workspace(
    val id: UUID,
    val title: String,
    val organizationId: UUID
) : Parcelable {

    constructor(apiModel: WorkspaceResponse) : this(
        id = apiModel.id ?: UUID.randomUUID(),
        title = apiModel.title ?: "",
        organizationId = apiModel.organiztionId ?: UUID.randomUUID()
    )
}