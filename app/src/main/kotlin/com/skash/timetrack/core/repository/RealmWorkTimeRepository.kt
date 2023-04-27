package com.skash.timetrack.core.repository

import com.skash.timetrack.core.cache.model.RealmWorkTime
import com.skash.timetrack.core.model.WorkTime
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.realm.Realm
import javax.inject.Inject

class RealmWorkTimeRepository @Inject constructor(
    private val scheduler: Scheduler
) : WorkTimeRepository {

    override fun createWorkTime(workTime: WorkTime): Observable<WorkTime> {
        return Observable.create { emitter ->
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                realm.insert(RealmWorkTime(workTime))
                realm.commitTransaction()

                emitter.onNext(workTime)
                emitter.onComplete()
            }
        }.subscribeOn(scheduler)
    }

    override fun fetchWorkTimes(): Observable<List<WorkTime>> {
        return Observable.create { emitter ->
            Realm.getDefaultInstance().use { realm ->
                val workTimes = realm.copyFromRealm(
                    realm.where(RealmWorkTime::class.java)
                        .sort("startedAt")
                        .findAll()
                ).map {
                    WorkTime(it)
                }

                emitter.onNext(workTimes)
            }
        }.subscribeOn(scheduler)
    }
}