package com.skash.timetrack.feature.manage.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Client
import com.skash.timetrack.core.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val clientsStateSubject = BehaviorSubject.create<State<List<Client>>>()
    private val clientsStateStream = clientsStateSubject.hide()
    private val _clientsStateLiveData = MutableLiveData<State<List<Client>>>()
    val clientsStateLiveData: LiveData<State<List<Client>>> get() = _clientsStateLiveData

    private val subscriptions = CompositeDisposable()

    init {
        clientsStateStream
            .subscribe(_clientsStateLiveData::postValue)
            .addTo(subscriptions)

        fetchClients()
    }

    private fun fetchClients() {
        val organizationId = UUID.randomUUID()/* application.getSelectedOrganizationUUID() ?: kotlin.run {
            clientsStateSubject.onNext(State.Error(ErrorType.NoWorkspaceSelected))
            return
        }
        */
        clientRepository.fetchClientsForOrganization(organizationId)
            .toState {
                ErrorType.ClientsFetch
            }
            .subscribe {
                clientsStateSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}