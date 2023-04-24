package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Project
import io.reactivex.rxjava3.core.Observable
import java.util.UUID

interface ProjectRepository {

    fun fetchProjects(): Observable<List<Project>>
    fun createProject(name: String, color: String, clientId: UUID): Observable<Project>
    fun updateProject(id: UUID, name: String, color: String, clientId: UUID): Observable<Project>
}