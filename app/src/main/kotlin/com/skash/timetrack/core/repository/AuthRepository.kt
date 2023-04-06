package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.AuthData
import io.reactivex.rxjava3.core.Observable

interface AuthRepository {
    fun login(email: String, password: String, twoFaCode: Int?): Observable<AuthData>
}