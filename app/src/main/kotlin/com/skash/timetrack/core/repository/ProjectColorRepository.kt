package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.ProjectColor
import io.reactivex.rxjava3.core.Observable

interface ProjectColorRepository {

    fun fetchColors(): Observable<List<ProjectColor>>
}