package com.skash.timetrack.feature.manage.team

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Member
import com.skash.timetrack.core.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val application: Application
) : ViewModel() {

    private val teamMemberStateSubject = BehaviorSubject.create<State<List<Member>>>()
    private val teamMemberStateStream = teamMemberStateSubject.hide()
    private val _teamMemberStateLiveData = MutableLiveData<State<List<Member>>>()
    val teamMemberStateLiveData: LiveData<State<List<Member>>> get() = _teamMemberStateLiveData

    private val subscriptions = CompositeDisposable()

    init {
        teamMemberStateStream
            .subscribe(_teamMemberStateLiveData::postValue)
            .addTo(subscriptions)

        fetchTeamMembers()
    }

    private fun fetchTeamMembers() {
        val workspaceUUID = UUID.randomUUID()/* application.getSelectedWorkspaceUUID() ?: kotlin.run {
            teamMemberStateSubject.onNext(State.Error(ErrorType.NoWorkspaceSelected))
            return
        }
        */

        teamRepository.fetchTeamMembersForWorkspace(workspaceUUID)
            .toState {
               ErrorType.WorkspaceMemberFetch
            }
            .subscribe { state ->
                teamMemberStateSubject.onNext(state)
            }
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}