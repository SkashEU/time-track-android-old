package com.skash.timetrack.feature.overview.worktime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.WorkTime
import com.skash.timetrack.core.model.WorkTimeGroup
import com.skash.timetrack.core.model.WorkTimeSection
import com.skash.timetrack.core.repository.WorkTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WorkTimeOverviewViewModel @Inject constructor(
    private val workTimeRepository: WorkTimeRepository
) : ViewModel() {

    private val workTimeGroupsSubject = BehaviorSubject.create<State<List<WorkTimeGroup>>>()
    private val workTimeGroupsStream = workTimeGroupsSubject.hide()
    private val _workTimeGroupsLiveData = MutableLiveData<State<List<WorkTimeGroup>>>()
    val workTimeGroupsLiveData: LiveData<State<List<WorkTimeGroup>>> get() = _workTimeGroupsLiveData

    private val workTimeCacheSubject = BehaviorSubject.create<List<WorkTime>>()

    private val subscriptions = CompositeDisposable()

    init {
        workTimeGroupsStream
            .subscribe(_workTimeGroupsLiveData::postValue)
            .addTo(subscriptions)
    }

    fun attachWorkTime(workTime: WorkTime) {
        val cachedWorkTimes = workTimeCacheSubject.value ?: emptyList()
        val updatedList = cachedWorkTimes + workTime
        workTimeCacheSubject.onNext(updatedList)
        workTimeGroupsSubject.onNext(State.Success(groupWorkTimes(updatedList)))
    }

    fun fetchWorkTimes() {
        workTimeRepository.fetchWorkTimes()
            .doOnNext {
                workTimeCacheSubject.onNext(it)
            }
            .flatMap {
                Observable.just(groupWorkTimes(it))
            }
            .toState {
                ErrorType.TaskFetch
            }
            .subscribe {
                workTimeGroupsSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    private fun groupWorkTimes(workTimes: List<WorkTime>): List<WorkTimeGroup> {
        // TODO: Deeper grouping by same project at same day etc
        val groupedTasks = workTimes.groupBy {
            it.startedAt.toInstant().truncatedTo(ChronoUnit.DAYS)
        }.map { (time, tasks) ->
            WorkTimeGroup(
                Date(time.toEpochMilli()),
                listOf(
                    WorkTimeSection(
                        Date(time.toEpochMilli()),
                        tasks
                    )
                )
            )
        }

        return groupedTasks
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}