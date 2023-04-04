package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Organization
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

class ApiOrganizationRepository : OrganizationRepository {

    override fun fetchSelfUserOrganizations(): Observable<List<Organization>> {
        return Observable.just(
            listOf(
                Organization(
                    UUID.randomUUID(),
                    "Test org"
                ),
                Organization(
                    UUID.randomUUID(),
                    "Test org"
                ),
                Organization(
                    UUID.randomUUID(),
                    "Test org"
                ),
                Organization(
                    UUID.randomUUID(),
                    "Test org"
                )
            )
        )
    }
}