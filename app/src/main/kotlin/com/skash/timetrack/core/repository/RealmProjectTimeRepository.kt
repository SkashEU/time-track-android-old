package com.skash.timetrack.core.repository

import com.skash.timetrack.core.cache.model.RealmProjectTime
import com.skash.timetrack.core.model.ProjectTime
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.realm.Realm
import javax.inject.Inject

class RealmProjectTimeRepository @Inject constructor(
    private val scheduler: Scheduler
) : ProjectTimeRepository {

    override fun createProjectTime(time: ProjectTime): Observable<Unit> {
        return Observable.create<Unit> { emitter ->
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                realm.insert(RealmProjectTime(time))
                realm.commitTransaction()

                emitter.onNext(Unit)
                emitter.onComplete()
            }
        }.subscribeOn(scheduler)
    }
}