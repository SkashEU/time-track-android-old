package com.skash.timetrack.feature.manage.project.manage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.skash.timetrack.TimeTrack
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.sharedprefs.getSelectedOrganizationUUID
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Client
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.model.ProjectColor
import com.skash.timetrack.core.model.ProjectModifyWrapper
import com.skash.timetrack.core.repository.ClientRepository
import com.skash.timetrack.core.repository.ProjectColorRepository
import com.skash.timetrack.core.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ManageProjectViewModel @Inject constructor(
    private val projectColorRepository: ProjectColorRepository,
    private val projectRepository: ProjectRepository,
    private val clientRepository: ClientRepository,
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {

    private val project = savedStateHandle.get<Project>("project")

    private val colorSubject = BehaviorSubject.create<List<ProjectColor>>()
    private val colorStream = colorSubject.hide()
    private val _colorLiveData = SingleLiveEvent<List<ProjectColor>>()
    val colorLiveData: LiveData<List<ProjectColor>> get() = _colorLiveData

    private val projectSubject = BehaviorSubject.create<Project>()
    private val projectStream = projectSubject.hide()
    private val _projectLiveData = SingleLiveEvent<Project>()
    val projectLiveData: LiveData<Project> get() = _projectLiveData

    private val projectStateSubject = PublishSubject.create<State<ProjectModifyWrapper>>()
    private val projectStateStream = projectStateSubject.hide()
    private val _projectStateLiveData = SingleLiveEvent<State<ProjectModifyWrapper>>()
    val projectStateLiveData: LiveData<State<ProjectModifyWrapper>> get() = _projectStateLiveData

    private val clientStateSubject = BehaviorSubject.create<State<List<Client>>>()
    private val clientStateStream = clientStateSubject.hide()
    private val _clientStateLiveData = MutableLiveData<State<List<Client>>>()
    val clientStateLiveData: LiveData<State<List<Client>>> get() = _clientStateLiveData

    private val clientPreselectSubject = BehaviorSubject.create<Client>()
    private val clientPreselectStream = clientPreselectSubject.hide()
    private val _clientPreselectLiveData = SingleLiveEvent<Client>()
    val clientPreselectLiveData: LiveData<Client> get() = _clientPreselectLiveData

    private val clientSubject = BehaviorSubject.create<Client>()

    private val selectedColorSubject = BehaviorSubject.create<ProjectColor>()

    private val subscriptions = CompositeDisposable()

    init {
        colorStream
            .subscribe(_colorLiveData::postValue)
            .addTo(subscriptions)

        projectStream
            .subscribe(_projectLiveData::postValue)
            .addTo(subscriptions)

        projectStateStream
            .subscribe(_projectStateLiveData::postValue)
            .addTo(subscriptions)

        clientStateStream
            .subscribe(_clientStateLiveData::postValue)
            .addTo(subscriptions)

        clientPreselectStream
            .subscribe(_clientPreselectLiveData::postValue)
            .addTo(subscriptions)

        // Project Model used to edit existing Project
        project?.let {
            projectSubject.onNext(it)
        }

        fetchColors()
        fetchClients()
    }

    fun createOrUpdateProject(title: String) {
        val selectedColor = selectedColorSubject.value?.hex ?: kotlin.run {
            projectStateSubject.onNext(State.Error(ErrorType.NoProjectColorSelected))
            return
        }

        val client = clientSubject.value ?: kotlin.run {
            projectStateSubject.onNext(State.Error(ErrorType.NoClientSelected))
            return
        }

        if (title.isEmpty()) {
            projectStateSubject.onNext(State.Error(ErrorType.NoProjectTitleSelected))
            return
        }

        project.let { existingProject ->

            if (existingProject == null) {
                createProject(title, selectedColor, client.id)
            } else if (existingProject.title == title && existingProject.color == selectedColor && existingProject.client.id == client.id) {
                Observable.just(ProjectModifyWrapper(existingProject, false))
            } else {
                updateProject(existingProject.id, title, selectedColor, client.id)
            }.toState {
                ErrorType.ProjectModify
            }.subscribe { state ->
                projectStateSubject.onNext(state)
            }.addTo(subscriptions)
        }
    }

    fun markProjectColorAsSelected(color: ProjectColor) {
        val colors = (colorSubject.value ?: emptyList())
        val index = colors.indexOf(color)

        if (index == -1) {
            Log.d(javaClass.name, "tried to mark non existing project as selected")
            return
        }

        colorSubject.onNext(colors.map {
            it.copy(isSelected = it == color)
        })
        selectedColorSubject.onNext(color)
    }

    private fun fetchColors() {
        projectColorRepository.fetchColors()
            .subscribeBy(
                onNext = {
                    colorSubject.onNext(it)

                    project?.let { selectedProject ->
                        markProjectColorAsSelected(ProjectColor(selectedProject.color))
                    }
                },
                onError = { error ->
                    Log.d(javaClass.name, "Failed to fetch project colors", error)
                }
            ).addTo(subscriptions)
    }

    private fun createProject(
        title: String,
        color: String,
        clientId: UUID
    ): Observable<ProjectModifyWrapper> {

        return projectRepository.createProject(title, color, clientId)
            .map {
                ProjectModifyWrapper(it, true)
            }
    }

    private fun updateProject(
        id: UUID,
        title: String,
        color: String,
        clientId: UUID
    ): Observable<ProjectModifyWrapper> {
        return projectRepository.updateProject(id, title, color, clientId)
            .map {
                ProjectModifyWrapper(it, false)
            }
    }

    private fun fetchClients() {
        val orgId = getApplication<TimeTrack>().getSelectedOrganizationUUID() ?: return
        clientRepository.fetchClientsForOrganization(orgId)
            .doOnNext {
                it.find { client ->
                    client.id == project?.client?.id
                }?.let { client ->
                    clientPreselectSubject.onNext(client)
                }
            }
            .toState {
                ErrorType.ClientsFetch
            }
            .subscribe {
                clientStateSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    fun setClient(client: Client) {
        clientSubject.onNext(client)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}