package com.skash.timetrack.core.repository

import com.skash.timetrack.core.cache.model.RealmTask
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.model.Task
import com.skash.timetrack.core.model.WorkTime
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.realm.Realm
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Inject

class RealmWorkTimeRepository @Inject constructor(
    private val scheduler: Scheduler
) : WorkTimeRepository {

    override fun createWorkTime(workTime: WorkTime): Observable<Unit> {
        TODO()
    }

    override fun fetchWorkTimes(): Observable<List<WorkTime>> {
        return Observable.just(
            listOf(
                WorkTime(1, Date(), Date(), 10),
                WorkTime(1, Date(), Date(), 101),
                WorkTime(1, Date(), Date(), 103),
                WorkTime(1, testDate(), Date(), 10),
            )
        )
    }

    private fun testDate(): Date {
        val now = Instant.now()
        return Date(now.minus(1, ChronoUnit.DAYS).toEpochMilli())
    }
}