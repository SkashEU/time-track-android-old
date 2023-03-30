package com.skash.timetrack.core.model

import androidx.annotation.StringRes
import com.skash.timetrack.R

enum class Role {
    ADMIN,
    MEMBER;

    @StringRes
    fun displayName(): Int {
        return when (this) {
            ADMIN -> R.string.role_admin_display_name
            MEMBER -> R.string.role_member_display_name
        }
    }
}