package com.skash.timetrack.core.repository

import android.content.Context
import com.skash.timetrack.api.network.api.UserApi
import com.skash.timetrack.api.network.api.WorktimeApi
import com.skash.timetrack.api.network.model.CreateWorktimeInput
import com.skash.timetrack.core.helper.sharedprefs.getAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.model.WorkTime
import io.reactivex.rxjava3.core.Observable
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Inject

class ApiWorkTimeRepository @Inject constructor(
    private val worktimeApi: WorktimeApi,
    private val userApi: UserApi,
    private val context: Context
) : WorkTimeRepository {

    override fun createWorkTime(workTime: WorkTime): Observable<WorkTime> {
        return worktimeApi.worktimePost(
            context.getPrefs().getAuthData().bearer,
            CreateWorktimeInput().apply {
                this.startedAt = workTime.startedAt
                this.endedAt = workTime.endedAt
                this.duration = workTime.duration
            }
        ).map {
            WorkTime(it)
        }
    }

    override fun fetchWorkTimes(): Observable<List<WorkTime>> {
        return userApi.usersMeWorktimeGet(context.getPrefs().getAuthData().bearer)
            .map { response ->
                response.map {
                    WorkTime(it)
                }
            }
    }

    private fun testDate(): Date {
        val now = Instant.now()
        return Date(now.minus(1, ChronoUnit.DAYS).toEpochMilli())
    }
}