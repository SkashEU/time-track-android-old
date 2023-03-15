package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.User
import io.reactivex.rxjava3.core.Observable

interface AuthRepository {

    fun login(email: String, password: String): Observable<User>

}