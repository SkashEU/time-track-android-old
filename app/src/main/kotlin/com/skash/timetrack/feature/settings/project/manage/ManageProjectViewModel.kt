package com.skash.timetrack.feature.settings.project.manage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.helper.rx.toState
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.State
import com.skash.timetrack.core.model.Project
import com.skash.timetrack.core.model.ProjectColor
import com.skash.timetrack.core.model.ProjectModifyWrapper
import com.skash.timetrack.core.repository.ProjectColorRepository
import com.skash.timetrack.core.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class ManageProjectViewModel @Inject constructor(
    private val projectColorRepository: ProjectColorRepository,
    private val projectRepository: ProjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

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

        // Project Model used to edit existing Project
        project?.let {
            projectSubject.onNext(it)
        }

        fetchColors()
    }

    fun createOrUpdateProject(title: String) {
        val selectedColor = selectedColorSubject.value?.hex ?: kotlin.run {
            projectStateSubject.onNext(State.Error(ErrorType.NoProjectColorSelected))
            return
        }

        if (title.isEmpty()) {
            projectStateSubject.onNext(State.Error(ErrorType.NoProjectTitleSelected))
            return
        }

        project.let { existingProject ->

            if (existingProject == null) {
                createProject(title, selectedColor)
            } else if (existingProject.title == title && existingProject.color == selectedColor) {
                Observable.just(ProjectModifyWrapper(existingProject, false))
            } else {
                updateProject(existingProject.id, title, selectedColor)
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
        color: String
    ): Observable<ProjectModifyWrapper> {
        val project = Project(title = title, color = color)
        return projectRepository.createProject(project)
            .map {
                ProjectModifyWrapper(project, true)
            }
    }

    private fun updateProject(
        id: Int,
        title: String,
        color: String
    ): Observable<ProjectModifyWrapper> {
        val project = Project(id, title, color)
        return projectRepository.updateProject(project)
            .map {
                ProjectModifyWrapper(project, false)
            }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}