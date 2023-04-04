package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.BackupCode
import io.reactivex.rxjava3.core.Observable

interface UserDataRepository {
    fun changeUsername(username: String): Observable<Unit>
    fun fetchBackupCodes(): Observable<List<BackupCode>>
}