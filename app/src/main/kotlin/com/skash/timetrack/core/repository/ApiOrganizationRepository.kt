package com.skash.timetrack.core.repository

import android.content.Context
import com.skash.timetrack.api.network.api.UserApi
import com.skash.timetrack.core.helper.sharedprefs.getAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.model.Organization
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ApiOrganizationRepository @Inject constructor(
    private val userApi: UserApi,
    private val context: Context
) : OrganizationRepository {

    override fun fetchSelfUserOrganizations(): Observable<List<Organization>> {
        return userApi.usersMeOrganizationsGet(context.getPrefs().getAuthData().bearer)
            .map { response ->
                response.map {
                    Organization(it)
                }
            }
    }
}