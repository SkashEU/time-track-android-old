package com.skash.timetrack.core.model

import android.os.Parcelable
import com.skash.timetrack.api.network.model.ClientResponse
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Client(
    val id: UUID,
    val title: String
) : Parcelable {

    constructor(apiModel: ClientResponse) : this(
        id = apiModel.id ?: UUID.randomUUID(),
        title = apiModel.title ?: ""
    )

    override fun toString(): String {
        return title
    }
}
