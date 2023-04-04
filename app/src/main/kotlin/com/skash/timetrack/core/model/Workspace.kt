package com.skash.timetrack.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Workspace(
    val id: UUID,
    val title: String
) : Parcelable