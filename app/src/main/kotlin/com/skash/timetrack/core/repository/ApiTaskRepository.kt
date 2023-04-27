package com.skash.timetrack.core.repository

import android.content.Context
import com.skash.timetrack.api.network.api.TaskApi
import com.skash.timetrack.api.network.api.UserApi
import com.skash.timetrack.api.network.model.CreateTaskInput
import com.skash.timetrack.core.helper.sharedprefs.getAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.model.Task
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject


class ApiTaskRepository @Inject constructor(
    private val taskApi: TaskApi,
    private val userApi: UserApi,
    private val context: Context
) : TaskRepository {

    override fun createTask(time: Task): Observable<Task> {
        return taskApi.tasksPost(context.getPrefs().getAuthData().bearer, CreateTaskInput().apply {
            this.startedAt = time.startedAt
            this.endedAt = time.endedAt
            this.duration = time.duration
            this.description = time.description
            this.project = time.project?.id
        }).map {
            Task(it)
        }
    }

    override fun fetchTasks(): Observable<List<Task>> {
        return userApi.usersMeTasksGet(context.getPrefs().getAuthData().bearer)
            .map { response ->
                response.map {
                    Task(it)
                }
            }
    }
}