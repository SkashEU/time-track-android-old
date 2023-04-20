package com.skash.timetrack.feature.auth.reset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.helper.string.isValidEmail
import com.skash.timetrack.core.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val emailValidationSubject = BehaviorSubject.create<State<Unit>>()
    private val emailValidationStream = emailValidationSubject.hide()
    private val _emailValidationLiveData = SingleLiveEvent<State<Unit>>()
    val emailValidationLiveData: LiveData<State<Unit>> get() = _emailValidationLiveData

    private val subscriptions = CompositeDisposable()

    init {
        emailValidationStream
            .subscribe(_emailValidationLiveData::postValue)
            .addTo(subscriptions)
    }

    fun resetPassword(email: String) {
        if (!email.isValidEmail()) {
            emailValidationSubject.onNext(State.Error(ErrorType.InvalidEmail))
            return
        }

        emailValidationSubject.onNext(State.Success(Unit))

        authRepository.resetPassword(email)
            .subscribeBy(
                onNext = {
                    Log.d(javaClass.name, "Password reset queued")
                },
                onError = {
                    Log.d(javaClass.name, "Reset failed. Ignoring...")
                }
            )
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}