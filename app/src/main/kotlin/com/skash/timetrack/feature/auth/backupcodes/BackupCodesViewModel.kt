package com.skash.timetrack.feature.auth.backupcodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.BackupCode
import com.skash.timetrack.core.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class BackupCodesViewModel @Inject constructor(
    private val userDataRepository: UserRepository
) : ViewModel() {

    private val backupCodesSubject = BehaviorSubject.create<State<List<BackupCode>>>()
    private val backupCodeStream = backupCodesSubject.hide()
    private val _backupCodeLiveData = SingleLiveEvent<State<List<BackupCode>>>()
    val backupCodeLiveData: LiveData<State<List<BackupCode>>> get() = _backupCodeLiveData

    private val subscriptions = CompositeDisposable()

    init {
        backupCodeStream
            .subscribe(_backupCodeLiveData::postValue)
            .addTo(subscriptions)
    }

    fun fetchBackupCodes(password: String) {
        userDataRepository.fetchBackupCodes(password)
            .toState {
                ErrorType.BackupCodesFetch
            }
            .subscribe {
                backupCodesSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}