package com.skash.timetrack.core.model

import com.skash.timetrack.api.network.model.UserResponse
import java.util.UUID

data class User(
    val id: UUID,
    val avatar: Avatar?,
    val email: String,
    val firstName: String,
    val lastName: String
) {

    val name: String by lazy {
        "$firstName $lastName"
    }

    constructor(apiModel: UserResponse) : this(
        id = apiModel.id ?: UUID.randomUUID(),
        avatar = apiModel.avatarPath?.let { Avatar(it) },
        email = apiModel.email ?: "",
        firstName = apiModel.firstName ?: "",
        lastName = apiModel.lastName ?: ""
    )
}
