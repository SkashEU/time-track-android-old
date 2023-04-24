package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Client
import com.skash.timetrack.core.model.Project
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

class ApiProjectRepository : ProjectRepository {

    override fun fetchProjects(): Observable<List<Project>> {
        return Observable.just(
            listOf(
                Project(
                    UUID.randomUUID(),
                    "Test 1",
                    "#54468d",
                    Client(UUID.randomUUID(), "Test Client")
                ),
                Project(
                    UUID.randomUUID(),
                    "Test 2",
                    "#b9e768",
                    Client(UUID.randomUUID(), "Test Client")
                ),
                Project(
                    UUID.randomUUID(),
                    "Test 3",
                    "#282828",
                    Client(UUID.randomUUID(), "Test Client")
                )
            )
        )
    }

    override fun createProject(name: String, color: String, clientId: UUID): Observable<Project> {
        TODO("Not yet implemented")
    }

    override fun updateProject(id: UUID, name: String, color: String, clientId: UUID): Observable<Project> {
        TODO("Not yet implemented")
    }
}