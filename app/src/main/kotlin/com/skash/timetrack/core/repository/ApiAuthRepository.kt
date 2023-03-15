package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.User
import io.reactivex.rxjava3.core.Observable

class ApiAuthRepository : AuthRepository {
    override fun login(email: String, password: String): Observable<User> {
        return Observable.just(User(1))
    }
}