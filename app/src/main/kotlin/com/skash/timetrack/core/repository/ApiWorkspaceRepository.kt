package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Workspace
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

class ApiWorkspaceRepository : WorkspaceRepository {
    override fun fetchWorkspacesForOrganizations(id: UUID): Observable<List<Workspace>> {
        return Observable.just(
            listOf(
                Workspace(
                    UUID.randomUUID(),
                    "Test $id"
                ),
                Workspace(
                    UUID.randomUUID(),
                    "Test $id"
                ),
                Workspace(
                    UUID.randomUUID(),
                    "Test $id"
                ),
                Workspace(
                    UUID.randomUUID(),
                    "Test $id"
                )
            )
        )
    }
}