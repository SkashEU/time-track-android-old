package com.skash.timetrack.core.menu

import androidx.annotation.StringRes
import com.skash.timetrack.R

enum class ProfileSectionEntry(@StringRes val titleId: Int) {
    USERNAME(R.string.profile_entry_username),
    EMAIL(R.string.profile_entry_email),
    PASSWORD(R.string.profile_entry_password),
    WORKSPACE(R.string.profile_entry_workspace),
    TWO_FACTOR_AUTH(R.string.profile_entry_two_factor_auth),
    BACKUP_CODES(R.string.profile_entry_backup_codes),
    MY_INVITES(R.string.profile_entry_my_invites)
}