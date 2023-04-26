package com.skash.timetrack.core.model

import com.skash.timetrack.BuildConfig
import com.skash.timetrack.api.network.model.AvatarResponse
import com.skash.timetrack.core.util.Constants

data class Avatar(
    val fileName: String
) {

    constructor(apiModel: AvatarResponse) : this(
        fileName = apiModel.avatarName ?: ""
    )

    fun avatarURL(): String {
        return BuildConfig.BASE_URL + Constants.AVATAR_URL_COMPONENT + fileName
    }
}