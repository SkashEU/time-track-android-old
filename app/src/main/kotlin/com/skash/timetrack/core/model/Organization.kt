package com.skash.timetrack.core.model

import com.skash.timetrack.api.network.model.OrganizationResponse
import java.util.UUID

data class Organization(
    val id: UUID,
    val name: String
) {

    constructor(apiModel: OrganizationResponse): this(
        id = apiModel.id ?: UUID.randomUUID(),
        name = apiModel.name ?: ""
    )
}