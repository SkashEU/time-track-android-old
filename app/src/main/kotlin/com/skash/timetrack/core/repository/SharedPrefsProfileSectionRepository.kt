package com.skash.timetrack.core.repository

import com.skash.timetrack.core.menu.ProfileSection
import com.skash.timetrack.core.menu.ProfileSectionEntry
import com.skash.timetrack.core.menu.ProfileSectionEntryType
import io.reactivex.rxjava3.core.Observable

class SharedPrefsProfileSectionRepository : ProfileSectionRepository {

    override fun fetchProfileSections(): Observable<List<ProfileSection>> {
        return Observable.just(
            listOf(
                ProfileSection.Workspace(
                    listOf(
                        ProfileSectionEntry(
                            ProfileSectionEntryType.WORKSPACE
                        )
                    )
                ),
                ProfileSection.AccountInformation(
                    listOf(
                        ProfileSectionEntry(
                            ProfileSectionEntryType.USERNAME
                        ),
                        ProfileSectionEntry(
                            ProfileSectionEntryType.EMAIL
                        ),
                        ProfileSectionEntry(
                            ProfileSectionEntryType.PASSWORD
                        )
                    )
                ),
                ProfileSection.TwoFactorAuthentication(
                    listOf(
                        ProfileSectionEntry(
                            ProfileSectionEntryType.BACKUP_CODES
                        ),
                        ProfileSectionEntry(
                            ProfileSectionEntryType.TWO_FACTOR_AUTH
                        )
                    )
                )
            )
        )

    }
}