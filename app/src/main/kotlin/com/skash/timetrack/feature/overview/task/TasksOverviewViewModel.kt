package com.skash.timetrack.feature.overview.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Task
import com.skash.timetrack.core.model.TaskGroup
import com.skash.timetrack.core.model.TaskSection
import com.skash.timetrack.core.repository.TaskCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TasksOverviewViewModel @Inject constructor(
    private val taskRepository: TaskCacheRepository
) : ViewModel() {

    private val taskGroupsSubject = BehaviorSubject.create<State<List<TaskGroup>>>()
    private val taskGroupsStream = taskGroupsSubject.hide()
    private val _taskGroupsLiveData = MutableLiveData<State<List<TaskGroup>>>()
    val taskGroupsLiveData: LiveData<State<List<TaskGroup>>> get() = _taskGroupsLiveData

    private val taskCacheSubject = BehaviorSubject.create<List<Task>>()

    private val subscriptions = CompositeDisposable()

    init {
        taskGroupsStream
            .subscribe(_taskGroupsLiveData::postValue)
            .addTo(subscriptions)
    }

    fun fetchTasks() {
        taskRepository.fetchTasks()
            .doOnNext {
                taskCacheSubject.onNext(it)
            }
            .flatMap {
                Observable.just(groupTasks(it))
            }
            .toState {
                ErrorType.TaskFetch
            }
            .subscribe {
                taskGroupsSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    private fun groupTasks(tasks: List<Task>): List<TaskGroup> {
        val groupedTasks = tasks.groupBy {
            it.startedAt.toInstant().truncatedTo(ChronoUnit.DAYS)
        }.map { (time, tasks) ->

            val sections = tasks.groupBy {
                "${it.description}-${it.project}"
            }.map {
                it.value
            }.map {
                TaskSection(
                    Date(time.toEpochMilli()),
                    it,
                    it.first().project,
                    it.first().description
                )
            }

            TaskGroup(
                Date(time.toEpochMilli()),
                sections
            )
        }

        return groupedTasks
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}