package com.skash.timetrack.core.repository

import android.content.Context
import com.skash.timetrack.api.network.api.UserApi
import com.skash.timetrack.api.network.model.UserInput
import com.skash.timetrack.core.helper.sharedprefs.getAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.model.Avatar
import com.skash.timetrack.core.model.BackupCode
import com.skash.timetrack.core.model.User
import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ApiUserRepository @Inject constructor(
    private val userApi: UserApi,
    private val context: Context
) : UserRepository {

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

    override fun changeAvatar(avatar: ByteArray): Observable<Avatar> {
        val request = avatar.toRequestBody("image/*".toMediaType())
        return userApi.usersMeAvatarPost(
            context.getPrefs().getAuthData().bearer,
            MultipartBody.Part.createFormData(
                "file",
                "avatar.jpg",
                request
            )
        ).map {
            Avatar(it)
        }
    }

    override fun registerUser(name: String, email: String, password: String): Observable<User> {
        return userApi.usersRegisterPost(UserInput().apply {
            this.email = email
            this.password = password
        }).map {
            User(it)
        }
    }
}