package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.ProjectTime
import io.reactivex.rxjava3.core.Observable

interface ProjectTimeRepository {
    fun createProjectTime(time: ProjectTime): Observable<Unit>
}