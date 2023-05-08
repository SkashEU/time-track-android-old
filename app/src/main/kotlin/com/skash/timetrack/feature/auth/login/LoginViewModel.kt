package com.skash.timetrack.feature.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.AuthData
import com.skash.timetrack.core.model.User
import com.skash.timetrack.core.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val authStateSubject = PublishSubject.create<State<AuthData>>()
    private val authStateStream = authStateSubject.hide()
    private val _authStateLiveData = SingleLiveEvent<State<AuthData>>()
    val authStateLiveData: LiveData<State<AuthData>> get() = _authStateLiveData

    private val subscriptions = CompositeDisposable()

    init {
        authStateStream
            .subscribe(_authStateLiveData::postValue)
            .addTo(subscriptions)
    }

    fun authenticateUser(email: String, password: String, twoFACode: Int? = null) {
        authRepository.login(email, password, twoFACode)
            .toState {
                ErrorType.UserUnauthenticated
            }
            .subscribe { authState ->
                authStateSubject.onNext(authState)
            }
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}