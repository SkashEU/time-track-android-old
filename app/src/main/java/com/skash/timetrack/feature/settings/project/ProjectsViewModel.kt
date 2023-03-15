package com.skash.timetrack.feature.settings.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val projectsSubject = BehaviorSubject.create<State<List<Project>>>()
    private val projectsStream = projectsSubject.hide()
    private val _projectsLiveData = SingleLiveEvent<State<List<Project>>>()
    val projectsLiveData: LiveData<State<List<Project>>> get() = _projectsLiveData

    private val subscriptions = CompositeDisposable()

    init {
        projectsStream
            .subscribe(_projectsLiveData::postValue)
            .addTo(subscriptions)

        fetchProjects()
    }

    private fun fetchProjects() {
        projectRepository.fetchProjects()
            .toState {
                ErrorType.ProjectFetch
            }
            .subscribe { projects ->
                projectsSubject.onNext(projects)
            }
            .addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}