package com.skash.timetrack.core.repository

import com.skash.timetrack.core.menu.ProfileSection
import com.skash.timetrack.core.menu.ProfileSectionEntry
import io.reactivex.rxjava3.core.Observable

class SharedPrefsProfileSectionRepository : ProfileSectionRepository {

    override fun fetchProfileSections(): Observable<List<ProfileSection>> {
        return Observable.just(
            listOf(
                ProfileSection.Workspace(
                    listOf(
                        ProfileSectionEntry.WORKSPACE
                    )
                ),
                ProfileSection.AccountInformation(
                    listOf(
                        ProfileSectionEntry.USERNAME,
                        ProfileSectionEntry.EMAIL,
                        ProfileSectionEntry.PASSWORD
                    )
                ),
                ProfileSection.TwoFactorAuthentication(
                    listOf(
                        ProfileSectionEntry.BACKUP_CODES,
                        ProfileSectionEntry.TWO_FACTOR_AUTH
                    )
                ),
                ProfileSection.Invites(
                    listOf(
                        ProfileSectionEntry.MY_INVITES
                    )
                )
            )
        )

    }
}