package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.AuthData
import com.skash.timetrack.core.model.DeviceInformation
import com.skash.timetrack.core.model.User
import io.reactivex.rxjava3.core.Observable

interface AuthRepository {
    fun login(email: String, password: String, twoFaCode: Int? = null): Observable<AuthData>
    fun resetPassword(email: String): Observable<Unit>
    fun fetchAuthenticatedUser(): Observable<User>
}