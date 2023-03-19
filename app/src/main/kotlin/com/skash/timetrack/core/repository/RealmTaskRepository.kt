package com.skash.timetrack.core.repository

import com.skash.timetrack.core.cache.model.RealmTask
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.model.Task
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.realm.Realm
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Inject


class RealmTaskRepository @Inject constructor(
    private val scheduler: Scheduler
) : TaskRepository {

    override fun createTask(time: Task): Observable<Unit> {
        return Observable.create<Unit> { emitter ->
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                realm.insert(RealmTask(time))
                realm.commitTransaction()

                emitter.onNext(Unit)
                emitter.onComplete()
            }
        }.subscribeOn(scheduler)
    }

    override fun fetchTasks(): Observable<List<Task>> {
        return Observable.just(
            listOf(
                Task(1, Project(1, "Test Project", "#f1c453"), "Test Time", Date(), Date(), 10),
                Task(
                    2,
                    Project(1, "Test Project", "#f1c453"),
                    "Test Time",
                    testDate(),
                    Date(),
                    10
                )
            )
        )
    }

    private fun testDate(): Date {
        val now = Instant.now()
        return Date(now.minus(1, ChronoUnit.DAYS).toEpochMilli())
    }
}