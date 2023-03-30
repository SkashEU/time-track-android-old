package com.skash.timetrack.core.model

import java.util.UUID

data class Member(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val role: Role
)