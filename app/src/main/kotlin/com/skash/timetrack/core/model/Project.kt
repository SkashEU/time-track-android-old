package com.skash.timetrack.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    val id: Int = 0,
    val title: String,
    val color: String
): Parcelable
