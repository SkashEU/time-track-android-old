package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.BackupCode
import io.reactivex.rxjava3.core.Observable

class ApiUserDataRepository : UserDataRepository {

    override fun changeUsername(username: String): Observable<Unit> {
        return Observable.just(Unit)
    }

    override fun fetchBackupCodes(): Observable<List<BackupCode>> {
        return Observable.just(
            listOf(
                BackupCode("ANENEN"),
                BackupCode("ANENEN"),
                BackupCode("ANENEN"),
                BackupCode("ANENEN")
            )
        )
    }
}