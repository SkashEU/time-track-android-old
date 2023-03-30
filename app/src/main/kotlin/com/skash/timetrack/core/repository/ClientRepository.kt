package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Client
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

interface ClientRepository {

    fun fetchClientsForOrganization(organizationId: UUID): Observable<List<Client>>
}