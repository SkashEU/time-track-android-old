package com.skash.timetrack.feature.timer.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.date.minusSeconds
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.model.Task
import com.skash.timetrack.core.model.TimerStatus
import com.skash.timetrack.core.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TaskTimerViewModel @Inject constructor(
    private val projectTimeRepository: TaskRepository
) : ViewModel() {

    private val timerStatusSubject = BehaviorSubject.create<TimerStatus>()
    private val timerStatusStream = timerStatusSubject.hide()
    private val _timerStatusLiveData = MutableLiveData<TimerStatus>()
    val timerStatusLiveData: LiveData<TimerStatus> get() = _timerStatusLiveData

    private val timerActionSubject = PublishSubject.create<Boolean>()
    private val timerActionStream = timerActionSubject.hide()
    private val _timerActionLiveData = MutableLiveData<Boolean>()
    val timerActionLiveData: LiveData<Boolean> get() = _timerActionLiveData

    private val projectTimeCreationStateSubject = PublishSubject.create<State<Unit>>()
    private val projectTimeCreationStateStream = projectTimeCreationStateSubject.hide()
    private val _projectTimeCreationStateLiveData = MutableLiveData<State<Unit>>()
    val projectTimeCreationStateLiveData: LiveData<State<Unit>> get() = _projectTimeCreationStateLiveData

    private val projectSubject = BehaviorSubject.create<Project>()

    private val subscriptions = CompositeDisposable()

    init {
        timerStatusStream
            .subscribe(_timerStatusLiveData::postValue)
            .addTo(subscriptions)

        timerActionStream
            .subscribe(_timerActionLiveData::postValue)
            .addTo(subscriptions)

        projectTimeCreationStateStream
            .subscribe(_projectTimeCreationStateLiveData::postValue)
            .addTo(subscriptions)
    }

    fun startOrStopTimer() {
        val isTimerRunning = (timerStatusSubject.value ?: TimerStatus(false, 0)).isTimerRunning
        timerActionSubject.onNext(isTimerRunning.not())
    }

    fun updateTimerStatus(status: TimerStatus) = timerStatusSubject.onNext(status)

    fun createProjectTime(description: String, duration: Int) {
        val project = projectSubject.value
        val now = Date()
        val time = Task(
            project = project,
            description = description,
            startedAt = now.minusSeconds(duration),
            endedAt = now,
            duration = duration
        )
        projectTimeRepository.createTask(time)
            .toState {
                ErrorType.ProjectTimeSave
            }
            .subscribe { creationState ->
                projectTimeCreationStateSubject.onNext(creationState)
            }
            .addTo(subscriptions)
    }
}