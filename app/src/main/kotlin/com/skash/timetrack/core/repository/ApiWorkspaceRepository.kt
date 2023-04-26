package com.skash.timetrack.core.repository

import android.content.Context
import com.skash.timetrack.api.network.api.WorkspaceApi
import com.skash.timetrack.core.helper.sharedprefs.getAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.model.Workspace
import io.reactivex.rxjava3.core.Observable
import java.util.UUID
import javax.inject.Inject

class ApiWorkspaceRepository @Inject constructor(
    private val workspaceApi: WorkspaceApi,
    private val context: Context
) : WorkspaceRepository {

    override fun fetchWorkspacesForOrganizations(id: UUID): Observable<List<Workspace>> {
        return workspaceApi.organizationsOrganizationIdWorkspacesGet(
            context.getPrefs().getAuthData().bearer,
            id
        ).map { response ->
            response.map {
                Workspace(it)
            }
        }
    }
}