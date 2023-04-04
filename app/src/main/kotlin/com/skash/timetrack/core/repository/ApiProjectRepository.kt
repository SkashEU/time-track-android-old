package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Project
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

class ApiProjectRepository : ProjectRepository {

    override fun fetchProjects(): Observable<List<Project>> {
        return Observable.just(
            listOf(
                Project(1, "Test 1", "#54468d", UUID.randomUUID()),
                Project(1, "Test 2", "#b9e768", UUID.randomUUID()),
                Project(1, "Test 3", "#282828", UUID.randomUUID())
            )
        )
    }

    override fun createProject(project: Project): Observable<Project> {
        TODO("Not yet implemented")
    }

    override fun updateProject(project: Project): Observable<Project> {
        TODO("Not yet implemented")
    }
}