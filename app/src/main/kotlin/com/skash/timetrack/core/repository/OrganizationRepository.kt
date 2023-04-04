package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Organization
import io.reactivex.rxjava3.core.Observable

interface OrganizationRepository {

    fun fetchSelfUserOrganizations(): Observable<List<Organization>>
}