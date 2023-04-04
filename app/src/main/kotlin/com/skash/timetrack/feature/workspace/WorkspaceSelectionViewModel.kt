package com.skash.timetrack.feature.workspace

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.skash.timetrack.TimeTrack
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.sharedprefs.getSelectedWorkspaceUUID
import com.skash.timetrack.core.helper.sharedprefs.saveSelectedOrganization
import com.skash.timetrack.core.helper.sharedprefs.saveSelectedWorkspace
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Organization
import com.skash.timetrack.core.model.Workspace
import com.skash.timetrack.core.model.wrapper.OrganizationSelectionWrapper
import com.skash.timetrack.core.model.wrapper.WorkspaceSelectionWrapper
import com.skash.timetrack.core.repository.OrganizationRepository
import com.skash.timetrack.core.repository.WorkspaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WorkspaceSelectionViewModel @Inject constructor(
    private val workspaceRepository: WorkspaceRepository,
    private val organizationRepository: OrganizationRepository,
    application: Application
) : AndroidViewModel(application) {

    private val selectedWorkspace = application.getSelectedWorkspaceUUID()
    private val selectedOrganization = application.getSelectedWorkspaceUUID()

    private val organizationsStateSubject =
        BehaviorSubject.create<State<List<OrganizationSelectionWrapper>>>()
    private val organizationsStateStream = organizationsStateSubject.hide()
    private val _organizationsStateLiveData =
        SingleLiveEvent<State<List<OrganizationSelectionWrapper>>>()
    val organizationsStateLiveData: LiveData<State<List<OrganizationSelectionWrapper>>> get() = _organizationsStateLiveData

    private val workspaceStateSubject =
        BehaviorSubject.create<State<List<WorkspaceSelectionWrapper>>>()
    private val workspaceStateStream = workspaceStateSubject.hide()
    private val _workspaceStateLiveData = SingleLiveEvent<State<List<WorkspaceSelectionWrapper>>>()
    val workspaceStateLiveData: LiveData<State<List<WorkspaceSelectionWrapper>>> get() = _workspaceStateLiveData

    private val saveStateSubject = BehaviorSubject.create<State<Workspace>>()
    private val saveStateStream = saveStateSubject.hide()
    private val _saveStateLiveData = SingleLiveEvent<State<Workspace>>()
    val saveStateLiveData: LiveData<State<Workspace>> get() = _saveStateLiveData

    private val subscriptions = CompositeDisposable()

    init {
        organizationsStateStream
            .subscribe(_organizationsStateLiveData::postValue)
            .addTo(subscriptions)

        workspaceStateStream
            .subscribe(_workspaceStateLiveData::postValue)
            .addTo(subscriptions)

        saveStateStream
            .subscribe(_saveStateLiveData::postValue)
            .addTo(subscriptions)

        fetchSelfUsersOrganizations()
    }

    fun saveSelectedOptions() {
        val context = getApplication<TimeTrack>()
        val workspace = getSelectedWorkspace()
        val org = getSelectedOrganization()

        if (org == null) {
            saveStateSubject.onNext(State.Error(ErrorType.NoOrganizationSelected))
            return
        }

        if (workspace == null) {
            saveStateSubject.onNext(State.Error(ErrorType.NoWorkspaceSelected))
            return
        }

        context.saveSelectedWorkspace(workspace)
        context.saveSelectedOrganization(org)

        saveStateSubject.onNext(State.Success(workspace))
    }

    fun markWorkspaceAsSelected(workspace: WorkspaceSelectionWrapper) {
        val workspaces = (workspaceStateSubject.value?.valueOrNull() ?: return).toMutableList()

        workspaces.find {
            it.isSelected
        }?.let { currentlySelectedWorkspace ->
            val currentlySelectedIndex = workspaces.indexOf(currentlySelectedWorkspace)
            workspaces[currentlySelectedIndex] =
                workspaces[currentlySelectedIndex].copy(isSelected = false)
        }

        val selectedIndex = workspaces.indexOf(workspace)

        if (selectedIndex == -1) {
            Log.d(javaClass.name, "Tried to mark non existing workspace as selected...")
            return
        }

        workspaces[selectedIndex] = workspaces[selectedIndex].copy(isSelected = true)
        workspaceStateSubject.onNext(State.Success(workspaces))
    }

    fun markOrganizationAsSelected(organization: OrganizationSelectionWrapper) {
        val organizations =
            (organizationsStateSubject.value?.valueOrNull() ?: return).toMutableList()

        organizations.find {
            it.isSelected
        }?.let { currentlySelectedOrganization ->
            val currentlySelectedIndex = organizations.indexOf(currentlySelectedOrganization)
            organizations[currentlySelectedIndex] =
                organizations[currentlySelectedIndex].copy(isSelected = false)
        }

        val selectedIndex = organizations.indexOf(organization)

        if (selectedIndex == -1) {
            Log.d(javaClass.name, "Tried to mark non existing organization as selected...")
            return
        }

        organizations[selectedIndex] = organizations[selectedIndex].copy(isSelected = true)
        organizationsStateSubject.onNext(State.Success(organizations))
    }

    fun fetchWorkspacesForOrganization(orgId: UUID) {
        workspaceRepository.fetchWorkspacesForOrganizations(orgId)
            .map {
                it.map { workspace ->
                    WorkspaceSelectionWrapper(
                        workspace,
                        isSelected = workspace.id == selectedWorkspace
                    )
                }
            }
            .toState {
                ErrorType.WorkspaceFetch
            }
            .subscribe {
                workspaceStateSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    private fun fetchSelfUsersOrganizations() {
        organizationRepository.fetchSelfUserOrganizations()
            .map {
                it.map { org ->
                    OrganizationSelectionWrapper(
                        org,
                        isSelected = org.id == selectedOrganization
                    )
                }
            }
            .toState {
                ErrorType.OrganizationFetch
            }
            .subscribe {
                organizationsStateSubject.onNext(it)
            }
            .addTo(subscriptions)
    }

    private fun getSelectedWorkspace(): Workspace? {
        val workspaces = workspaceStateSubject.value?.valueOrNull() ?: return null

        return workspaces.find {
            it.isSelected
        }?.workspace
    }

    private fun getSelectedOrganization(): Organization? {
        val orgs = organizationsStateSubject.value?.valueOrNull() ?: return null

        return orgs.find {
            it.isSelected
        }?.organization
    }


}