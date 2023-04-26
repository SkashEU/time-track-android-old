package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.Avatar
import com.skash.timetrack.core.model.BackupCode
import com.skash.timetrack.core.model.User
import io.reactivex.rxjava3.core.Observable

interface UserRepository {
    fun changeUsername(username: String): Observable<Unit>
    fun fetchBackupCodes(): Observable<List<BackupCode>>
    fun changeAvatar(avatar: ByteArray): Observable<Avatar>
    fun registerUser(name: String, email: String, password: String): Observable<User>
}