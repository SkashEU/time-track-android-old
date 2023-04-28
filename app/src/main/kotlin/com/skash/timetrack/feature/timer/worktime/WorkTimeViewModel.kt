package com.skash.timetrack.feature.timer.worktime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.date.minusSeconds
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.TimerStatus
import com.skash.timetrack.core.model.WorkTime
import com.skash.timetrack.core.repository.WorkTimeCacheRepository
import com.skash.timetrack.core.repository.WorkTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WorkTimeViewModel @Inject constructor(
    private val workTimeRepository: WorkTimeRepository,
    private val workTimeCacheRepository: WorkTimeCacheRepository
) : ViewModel() {

    private val timerStatusSubject = BehaviorSubject.create<TimerStatus>()
    private val timerStatusStream = timerStatusSubject.hide()
    private val _timerStatusLiveData = MutableLiveData<TimerStatus>()
    val timerStatusLiveData: LiveData<TimerStatus> get() = _timerStatusLiveData

    private val timerActionSubject = PublishSubject.create<Boolean>()
    private val timerActionStream = timerActionSubject.hide()
    private val _timerActionLiveData = MutableLiveData<Boolean>()
    val timerActionLiveData: LiveData<Boolean> get() = _timerActionLiveData

    private val workTimeCreationStateSubject = PublishSubject.create<State<WorkTime>>()
    private val workTimeCreationStateStream = workTimeCreationStateSubject.hide()
    private val _workTimeCreationStateLiveData = MutableLiveData<State<WorkTime>>()
    val workTimeCreationStateLiveData: LiveData<State<WorkTime>> get() = _workTimeCreationStateLiveData

    private val subscriptions = CompositeDisposable()

    init {
        timerStatusStream
            .subscribe(_timerStatusLiveData::postValue)
            .addTo(subscriptions)

        timerActionStream
            .subscribe(_timerActionLiveData::postValue)
            .addTo(subscriptions)

        workTimeCreationStateStream
            .subscribe(_workTimeCreationStateLiveData::postValue)
            .addTo(subscriptions)
    }

    fun startOrStopTimer() {
        val isTimerRunning = (timerStatusSubject.value ?: TimerStatus(false, 0)).isTimerRunning
        timerActionSubject.onNext(isTimerRunning.not())
    }

    fun updateTimerStatus(status: TimerStatus) = timerStatusSubject.onNext(status)

    fun createWorkTime(duration: Int) {
        val now = Date()
        val time = WorkTime(
            startedAt = now.minusSeconds(duration),
            endedAt = now,
            duration = duration
        )
        workTimeRepository.createWorkTime(time)
            .flatMap { workTime ->
                workTimeCacheRepository.cacheWorkTime(listOf(workTime))
                    .map {
                        workTime
                    }
            }
            .toState {
                ErrorType.ProjectTimeSave
            }
            .subscribe { creationState ->
                workTimeCreationStateSubject.onNext(creationState)
            }
            .addTo(subscriptions)
    }
}