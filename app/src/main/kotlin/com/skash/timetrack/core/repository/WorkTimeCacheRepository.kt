package com.skash.timetrack.core.repository

import com.skash.timetrack.core.model.WorkTime
import io.reactivex.rxjava3.core.Observable

interface WorkTimeCacheRepository {
    fun cacheWorkTime(workTime: List<WorkTime>): Observable<Unit>
    fun fetchWorkTime(): Observable<List<WorkTime>>
    fun clearCache(): Observable<Unit>
}