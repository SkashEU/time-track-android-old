package com.skash.timetrack.core.repository

import com.skash.timetrack.core.menu.ProfileSection
import io.reactivex.rxjava3.core.Observable

interface ProfileSectionRepository {

    fun fetchProfileSections(): Observable<List<ProfileSection>>
}