package com.skash.timetrack.core.menu

import androidx.annotation.StringRes
import com.skash.timetrack.R

sealed class ProfileSection(@StringRes val titleId: Int, val items: List<ProfileSectionEntry>) {

    class AccountInformation(items: List<ProfileSectionEntry>) : ProfileSection(
        R.string.profile_section_account_information, items
    )

    class TwoFactorAuthentication(items: List<ProfileSectionEntry>) : ProfileSection(
        R.string.profile_section_two_factor_auth, items
    )

    class Workspace(items: List<ProfileSectionEntry>) : ProfileSection(
        R.string.profile_section_workspace, items
    )

    class Invites(items: List<ProfileSectionEntry>): ProfileSection(
        R.string.profile_section_invites, items
    )
}
