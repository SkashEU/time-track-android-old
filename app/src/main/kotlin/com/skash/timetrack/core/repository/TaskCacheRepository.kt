package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Task
import io.reactivex.rxjava3.core.Observable

interface TaskCacheRepository {

    fun cacheTasks(tasks: List<Task>): Observable<Unit>
    fun fetchTasks(): Observable<List<Task>>
    fun clearCache(): Observable<Unit>
}