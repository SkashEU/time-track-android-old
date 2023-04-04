package com.skash.timetrack.feature.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skash.timetrack.core.helper.livedata.SingleLiveEvent
import com.skash.timetrack.core.menu.ProfileSection
import com.skash.timetrack.core.menu.ProfileSectionEntryType
import com.skash.timetrack.core.repository.ProfileSectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileSectionRepository: ProfileSectionRepository
) : ViewModel() {

    private val profileSectionSubject = BehaviorSubject.create<List<ProfileSection>>()
    private val profileSectionStream = profileSectionSubject.hide()
    private val _profileSectionLiveData = MutableLiveData<List<ProfileSection>>()
    val profileSectionLiveData: LiveData<List<ProfileSection>> get() = _profileSectionLiveData

    private val profileSelectionSubject = BehaviorSubject.create<ProfileSectionEntryType>()
    private val profileSelectionStream = profileSelectionSubject.hide()
    private val _profileSelectionLiveData = SingleLiveEvent<ProfileSectionEntryType>()
    val profileSelectionLiveData: LiveData<ProfileSectionEntryType> get() = _profileSelectionLiveData

    private val subscriptions = CompositeDisposable()

    init {
        profileSectionStream
            .subscribe(_profileSectionLiveData::postValue)
            .addTo(subscriptions)

        profileSelectionStream
            .subscribe(_profileSelectionLiveData::postValue)
            .addTo(subscriptions)

        fetchProfileSections()
    }

    fun onProfileSectionClicked(entry: ProfileSectionEntryType) {
        profileSelectionSubject.onNext(entry)
    }

    fun fetchProfileSections() {
        profileSectionRepository
            .fetchProfileSections()
            .subscribeBy(
                onNext = {
                    profileSectionSubject.onNext(it)
                },
                onError = {
                    Log.d(javaClass.name, "Failed to fetch profile sections", it)
                }
            ).addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}