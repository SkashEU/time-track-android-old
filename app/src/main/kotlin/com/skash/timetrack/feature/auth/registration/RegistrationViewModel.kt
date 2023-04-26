package com.skash.timetrack.feature.auth.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.AuthData
import com.skash.timetrack.core.repository.AuthRepository
import com.skash.timetrack.core.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val registrationStateSubject = BehaviorSubject.create<State<AuthData>>()
    private val registrationStateStream = registrationStateSubject.hide()
    private val _registrationStateLiveData = SingleLiveEvent<State<AuthData>>()
    val registrationStateLiveData: LiveData<State<AuthData>> get() = _registrationStateLiveData

    private val subscriptions = CompositeDisposable()

    init {
        registrationStateStream
            .subscribe(_registrationStateLiveData::postValue)
            .addTo(subscriptions)
    }

    fun registerUser(name: String, email: String, password: String) {
        userRepository.registerUser(name, email, password)
            .flatMap {
                authRepository.login(email, password)
            }
            .toState {
                ErrorType.Registration
            }
            .subscribe {
                registrationStateSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}