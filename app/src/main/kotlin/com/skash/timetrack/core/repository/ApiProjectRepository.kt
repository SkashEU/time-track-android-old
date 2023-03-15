package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Project
import io.reactivex.rxjava3.core.Observable

class ApiProjectRepository : ProjectRepository {

    override fun fetchProjects(): Observable<List<Project>> {
        return Observable.just(
            listOf(
                Project(1, "Test 1", "#FFFFFF"),
                Project(1, "Test 2", "#FFFFAB"),
                Project(1, "Test 3", "#FFABFF")
            )
        )
    }
}