package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Project
import io.reactivex.rxjava3.core.Observable

interface ProjectRepository {

    fun fetchProjects(): Observable<List<Project>>
}