package com.skash.timetrack.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Client(
    val id: UUID,
    val title: String
) : Parcelable {

    override fun toString(): String {
        return title
    }
}
