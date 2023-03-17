package com.skash.timetrack.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProjectModifyWrapper(
    val project: Project,
    val isNew: Boolean
) : Parcelable