package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Task
import io.reactivex.rxjava3.core.Observable

interface TaskRepository {
    fun createTask(time: Task): Observable<Unit>
    fun fetchTasks(): Observable<List<Task>>
}