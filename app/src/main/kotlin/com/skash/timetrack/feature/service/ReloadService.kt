package com.skash.timetrack.feature.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.skash.timetrack.core.exception.MissingTimerActionException
import com.skash.timetrack.core.repository.WorkTimeCacheRepository
import com.skash.timetrack.core.repository.WorkTimeRepository
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@AndroidEntryPoint
class ReloadService : Service() {

    @Inject
    lateinit var workTimeCacheRepository: WorkTimeCacheRepository

    @Inject
    lateinit var workTimeRepository: WorkTimeRepository

    private val subscriptions = CompositeDisposable()

    companion object {
        const val RELOAD_ACTION = "reload_action"

        const val RELOAD_WORK_TIME = "reload_work_time"
        const val RELOAD_TASKS = "reload_tasks"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.getStringExtra(RELOAD_ACTION)) {
            RELOAD_WORK_TIME -> reloadWorkTime()
            RELOAD_TASKS -> reloadTasks()
            else -> throw MissingTimerActionException("Cant start service without a action!")
        }

        return START_STICKY
    }

    private fun reloadWorkTime() {
        workTimeRepository.fetchWorkTimes()
            .flatMap { workTime ->
                workTimeCacheRepository.clearCache()
                    .map {
                        workTime
                    }
            }
            .flatMap {
                workTimeCacheRepository.cacheWorkTime(it)
            }
            .subscribeBy(
                onNext = {
                    Log.d(javaClass.name, "Successfully reloaded workTime")
                },
                onError = {
                    Log.d(javaClass.name, "Failed to reloaded workTime", it)
                }
            ).addTo(subscriptions)
    }

    private fun reloadTasks() {

    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}