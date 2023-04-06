package com.skash.timetrack.core.repository

import com.skash.timetrack.api.network.api.AuthApi
import com.skash.timetrack.core.helper.string.encode
import com.skash.timetrack.core.model.AuthData
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ApiAuthRepository @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override fun login(email: String, password: String, twoFaCode: Int?): Observable<AuthData> {
        val authData = "Basic " + "${email.trim()}:${password.trim()}".encode()
        return authApi.authLoginPost(authData, twoFaCode)
            .map {
                AuthData("Bearer ${it.token ?: ""}")
            }
    }

}