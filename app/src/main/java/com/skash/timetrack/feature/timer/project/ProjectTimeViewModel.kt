package com.skash.timetrack.feature.timer.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.model.TimerStatus
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ProjectTimeViewModel : ViewModel() {

    private val timerStatusSubject = BehaviorSubject.create<TimerStatus>()
    private val timerStatusStream = timerStatusSubject.hide()
    private val _timerStatusLiveData = SingleLiveEvent<TimerStatus>()
    val timerStatusLiveData: LiveData<TimerStatus> get() = _timerStatusLiveData

    private val timerActionSubject = BehaviorSubject.createDefault(false)
    private val timerActionStream = timerActionSubject.hide()
    private val _timerActionLiveData = SingleLiveEvent<Boolean>()
    val timerActionLiveData: LiveData<Boolean> get() = _timerActionLiveData

    private val subscriptions = CompositeDisposable()

    init {
        timerStatusStream
            .subscribe(_timerStatusLiveData::postValue)
            .addTo(subscriptions)

        timerActionStream
            .subscribe(_timerActionLiveData::postValue)
            .addTo(subscriptions)
    }

    fun startOrStopTimer() {
        val isTimerRunning = (timerStatusSubject.value ?: TimerStatus(false, 0)).isTimerRunning
        timerActionSubject.onNext(isTimerRunning.not())
    }

    fun updateTimerStatus(status: TimerStatus) = timerStatusSubject.onNext(status)

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}