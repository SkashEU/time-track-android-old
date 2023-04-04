package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Workspace
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

interface WorkspaceRepository {

    fun fetchWorkspacesForOrganizations(id: UUID): Observable<List<Workspace>>
}