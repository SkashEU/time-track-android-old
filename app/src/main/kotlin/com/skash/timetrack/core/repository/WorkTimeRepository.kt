package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.WorkTime
import io.reactivex.rxjava3.core.Observable

interface WorkTimeRepository {
    fun createWorkTime(workTime: WorkTime): Observable<WorkTime>
    fun fetchWorkTimes(): Observable<List<WorkTime>>
}