package com.skash.timetrack.feature.profile.username

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.skash.timetrack.TimeTrack
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.sharedprefs.saveUsername
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class NameChangeViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    application: Application
) : AndroidViewModel(application) {

    private val usernameStateSubject = BehaviorSubject.create<State<Unit>>()
    private val usernameStateStream = usernameStateSubject.hide()
    private val _usernameStateLiveData = SingleLiveEvent<State<Unit>>()
    val usernameStateLiveData: LiveData<State<Unit>> get() = _usernameStateLiveData

    private val subscriptions = CompositeDisposable()

    init {
        usernameStateStream
            .subscribe(_usernameStateLiveData::postValue)
            .addTo(subscriptions)
    }

    fun changeUsername(username: String) {
        if (username.isEmpty()) {
            usernameStateSubject.onNext(State.Error(ErrorType.UsernameEmpty))
            return
        }

        userDataRepository.changeUsername(username)
            .doOnNext {
                getApplication<TimeTrack>().saveUsername(username)
            }
            .toState {
                ErrorType.UsernameChange
            }
            .subscribe {
                usernameStateSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}