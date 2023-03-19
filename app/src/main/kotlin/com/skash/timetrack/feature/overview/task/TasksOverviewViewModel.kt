package com.skash.timetrack.feature.overview.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Task
import com.skash.timetrack.core.model.TaskGroup
import com.skash.timetrack.core.repository.TaskRepository
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
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val taskGroupsSubject = BehaviorSubject.create<State<List<TaskGroup>>>()
    private val taskGroupsStream = taskGroupsSubject.hide()
    private val _taskGroupsLiveData = SingleLiveEvent<State<List<TaskGroup>>>()
    val taskGroupsLiveData: LiveData<State<List<TaskGroup>>> get() = _taskGroupsLiveData

    private val subscriptions = CompositeDisposable()

    init {
        taskGroupsStream
            .subscribe(_taskGroupsLiveData::postValue)
            .addTo(subscriptions)

        fetchTasks()
    }

    private fun fetchTasks() {
        taskRepository.fetchTasks()
            .flatMap {
                groupTasks(it)
            }
            .toState {
                ErrorType.TaskFetch
            }
            .subscribe {
                taskGroupsSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    private fun groupTasks(tasks: List<Task>): Observable<List<TaskGroup>> {
        // TODO: Deeper grouping by same project at same day etc
        val groupedTasks = tasks.groupBy {
            val test = it.startedAt.toInstant().truncatedTo(ChronoUnit.DAYS)
            test
        }.map { (time, tasks) ->
            TaskGroup(
                Date(time.toEpochMilli()),
                tasks
            )
        }

        return Observable.just(groupedTasks)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}