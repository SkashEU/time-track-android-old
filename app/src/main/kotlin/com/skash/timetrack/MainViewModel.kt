package com.skash.timetrack

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.User
import com.skash.timetrack.core.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val authenticatedUserSubject = BehaviorSubject.create<State<User>>()
    private val authenticatedUserStream = authenticatedUserSubject.hide()
    private val _authenticatedUserLiveData = SingleLiveEvent<State<User>>()
    val authenticatedUserLiveData: LiveData<State<User>> get() = _authenticatedUserLiveData

    private val subscriptions = CompositeDisposable()

    init {
        authenticatedUserStream
            .subscribe(_authenticatedUserLiveData::postValue)
            .addTo(subscriptions)

        fetchAuthenticatedUser()
    }

    private fun fetchAuthenticatedUser() {
        authRepository.fetchAuthenticatedUser()
            .toState {
                ErrorType.AuthenticatedUserFetch
            }
            .subscribe {
                authenticatedUserSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}