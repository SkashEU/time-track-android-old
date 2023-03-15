package com.skash.timetrack

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.skash.timetrack.feature.service.ProjectTimerService
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class TimeTrack : Application() {

    override fun onCreate() {
        super.onCreate()

        setupRealm()
        createProjectTimeNotificationChannel()
    }

    private fun setupRealm() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .allowQueriesOnUiThread(false)
            .allowWritesOnUiThread(false)
            .build()

        Realm.setDefaultConfiguration(config)
    }

    private fun createProjectTimeNotificationChannel() {
        val notificationChannel = NotificationChannel(
            ProjectTimerService.CHANNEL_ID,
            getString(R.string.title_project_time_channel),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(true)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}