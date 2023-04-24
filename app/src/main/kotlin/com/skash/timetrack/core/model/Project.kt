package com.skash.timetrack.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Project(
    val id: UUID,
    val title: String,
    val color: String,
    val client: Client
) : Parcelable
