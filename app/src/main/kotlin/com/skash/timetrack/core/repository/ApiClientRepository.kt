package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Client
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

class ApiClientRepository : ClientRepository {

    override fun fetchClientsForOrganization(organizationId: UUID): Observable<List<Client>> {
        return Observable.just(
            listOf(
                Client(organizationId, "Test Client"),
                Client(organizationId, "Test Client 1"),
                Client(organizationId, "Test Client 2")
            )
        )
    }
}