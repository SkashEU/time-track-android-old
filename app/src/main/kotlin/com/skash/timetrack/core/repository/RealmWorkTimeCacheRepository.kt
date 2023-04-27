package com.skash.timetrack.core.repository

import com.skash.timetrack.core.cache.model.RealmWorkTime
import com.skash.timetrack.core.model.WorkTime
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.realm.Realm
import javax.inject.Inject

class RealmWorkTimeCacheRepository @Inject constructor(
    private val scheduler: Scheduler
) : WorkTimeCacheRepository {

    override fun cacheWorkTime(workTime: List<WorkTime>): Observable<Unit> {
        return Observable.create { emitter ->
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                realm.insert(workTime.map {
                    RealmWorkTime(it)
                })
                realm.commitTransaction()

                emitter.onNext(Unit)
                emitter.onComplete()
            }
        }.subscribeOn(scheduler)
    }

    override fun fetchWorkTime(): Observable<List<WorkTime>> {
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

    override fun clearCache(): Observable<Unit> {
        return Observable.create { emitter ->
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                realm.delete(RealmWorkTime::class.java)
                realm.commitTransaction()

                emitter.onNext(Unit)
                emitter.onComplete()
            }
        }
    }
}