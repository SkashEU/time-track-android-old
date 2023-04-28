package com.skash.timetrack.core.repository

import com.skash.timetrack.core.cache.model.RealmTask
import com.skash.timetrack.core.model.Task
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.realm.Realm
import io.realm.Sort
import javax.inject.Inject

class RealmTaskCacheRepository @Inject constructor(
    private val scheduler: Scheduler
) : TaskCacheRepository {

    override fun cacheTasks(tasks: List<Task>): Observable<Unit> {
        return Observable.create { emitter ->
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                realm.insert(tasks.map {
                    RealmTask(it)
                })
                realm.commitTransaction()

                emitter.onNext(Unit)
                emitter.onComplete()
            }
        }.subscribeOn(scheduler)
    }

    override fun fetchTasks(): Observable<List<Task>> {
        return Observable.create { emitter ->
            Realm.getDefaultInstance().use { realm ->
                val workTimes = realm.copyFromRealm(
                    realm.where(RealmTask::class.java)
                        .sort("startedAt", Sort.DESCENDING)
                        .findAll()
                ).map {
                    Task(it)
                }

                emitter.onNext(workTimes)
                emitter.onComplete()
            }
        }.subscribeOn(scheduler)
    }

    override fun clearCache(): Observable<Unit> {
        return Observable.create { emitter ->
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                realm.delete(RealmTask::class.java)
                realm.commitTransaction()

                emitter.onNext(Unit)
                emitter.onComplete()
            }
        }
    }
}