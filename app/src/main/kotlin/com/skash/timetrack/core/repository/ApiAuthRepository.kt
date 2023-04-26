package com.skash.timetrack.core.repository

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.skash.timetrack.api.network.api.AuthApi
import com.skash.timetrack.api.network.model.Device
import com.skash.timetrack.core.helper.sharedprefs.getAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.helper.string.encode
import com.skash.timetrack.core.model.AuthData
import com.skash.timetrack.core.model.DeviceInformation
import com.skash.timetrack.core.model.User
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ApiAuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val context: Context
) : AuthRepository {

    override fun login(
        email: String,
        password: String,
        twoFaCode: Int?
    ): Observable<AuthData> {
        val authData = "Basic " + "${email.trim()}:${password.trim()}".encode()

        return fetchPushToken()
            .flatMap { pushToken ->
                authApi.authLoginPost(authData, twoFaCode, Device().apply {
                    this.model = DeviceInformation.model
                    this.manufacturer = DeviceInformation.manufacturer
                    this.osVersion = DeviceInformation.osVersion
                    this.pushToken = pushToken
                })
            }.map {
                AuthData("Bearer ${it.token ?: ""}")
            }
    }

    override fun resetPassword(email: String): Observable<Unit> {
        return Observable.just(Unit)
    }

    override fun fetchAuthenticatedUser(): Observable<User> {
        return authApi.authMeGet(context.getPrefs().getAuthData().bearer)
            .map {
                User(it)
            }
    }

    private fun fetchPushToken(): Observable<String> {
        return Observable.create { emitter ->
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener {
                    emitter.onNext(it)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}