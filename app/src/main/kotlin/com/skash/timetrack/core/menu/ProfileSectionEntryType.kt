package com.skash.timetrack.core.menu

import androidx.annotation.StringRes
import com.skash.timetrack.R

enum class ProfileSectionEntryType(@StringRes val titleId: Int) {
    USERNAME(R.string.profile_entry_username),
    EMAIL(R.string.profile_entry_email),
    PASSWORD(R.string.profile_entry_password),
    WORKSPACE(R.string.profile_entry_workspace),
    TWO_FACTOR_AUTH(R.string.profile_entry_two_factor_auth)
}