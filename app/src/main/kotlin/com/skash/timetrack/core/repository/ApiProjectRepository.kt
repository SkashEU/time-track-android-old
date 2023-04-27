package com.skash.timetrack.core.repository

import android.content.Context
import com.skash.timetrack.api.network.api.ProjectApi
import com.skash.timetrack.api.network.model.CreateProjectInput
import com.skash.timetrack.core.helper.sharedprefs.getAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.model.Project
import io.reactivex.rxjava3.core.Observable
import java.util.UUID
import javax.inject.Inject

class ApiProjectRepository @Inject constructor(
    private val projectApi: ProjectApi,
    private val context: Context
) : ProjectRepository {

    override fun fetchProjects(): Observable<List<Project>> {
        return projectApi.projectsGet(context.getPrefs().getAuthData().bearer)
            .map { response ->
                response.map {
                    Project(it)
                }
            }
    }

    override fun createProject(name: String, color: String, clientId: UUID): Observable<Project> {
        return projectApi.projectsPost(
            context.getPrefs().getAuthData().bearer,
            CreateProjectInput().apply {
                this.title = name
                this.color = color
                this.clientId = clientId
            }
        ).map {
            Project(it)
        }
    }

    override fun updateProject(
        id: UUID,
        name: String,
        color: String,
        clientId: UUID
    ): Observable<Project> {
        TODO("Not yet implemented")
    }
}