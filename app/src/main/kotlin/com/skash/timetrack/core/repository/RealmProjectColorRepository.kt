package com.skash.timetrack.core.repository

import com.skash.timetrack.core.cache.model.RealmProjectColor
import com.skash.timetrack.core.model.ProjectColor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.realm.Realm
import javax.inject.Inject

class RealmProjectColorRepository @Inject constructor(
    private val scheduler: Scheduler
) : ProjectColorRepository {
    override fun fetchColors(): Observable<List<ProjectColor>> {

        return Observable.create { emitter ->
            Realm.getDefaultInstance().use { realm ->
                val result = realm.copyFromRealm(
                    realm.where(RealmProjectColor::class.java).findAll()
                ).map {
                    ProjectColor(it.hex)
                }

                emitter.onNext(result)
                emitter.onComplete()
            }
        }.subscribeOn(scheduler)
    }
}