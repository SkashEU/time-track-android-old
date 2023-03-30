package com.skash.timetrack.core.repository

import android.content.Context
import com.skash.timetrack.core.helper.sharedprefs.getUserEmail
import com.skash.timetrack.core.helper.sharedprefs.getUserName
import com.skash.timetrack.core.helper.sharedprefs.getWorkspaceTitle
import com.skash.timetrack.core.menu.ProfileSection
import com.skash.timetrack.core.menu.ProfileSectionEntry
import com.skash.timetrack.core.menu.ProfileSectionEntryType
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SharedPrefsProfileSectionRepository @Inject constructor(
    private val context: Context
) : ProfileSectionRepository {

    override fun fetchProfileSections(): Observable<List<ProfileSection>> {
        return Observable.just(
            listOf(
                ProfileSection.Workspace(
                    listOf(
                        ProfileSectionEntry(
                            ProfileSectionEntryType.WORKSPACE,
                            context.getWorkspaceTitle()
                        )
                    )
                ),
                ProfileSection.AccountInformation(
                    listOf(
                        ProfileSectionEntry(
                            ProfileSectionEntryType.USERNAME,
                            context.getUserName()
                        ),
                        ProfileSectionEntry(
                            ProfileSectionEntryType.EMAIL,
                            context.getUserEmail()
                        ),
                        ProfileSectionEntry(
                            ProfileSectionEntryType.PASSWORD,
                            ""
                        )
                    )
                ),
                ProfileSection.TwoFactorAuthentication(
                    listOf(
                        ProfileSectionEntry(
                            ProfileSectionEntryType.TWO_FACTOR_AUTH,
                            ""
                        )
                    )
                )
            )
        )

    }
}