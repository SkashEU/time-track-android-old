package com.skash.timetrack

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.skash.timetrack.feature.service.ProjectTimeForegroundService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TimeTrack : Application() {

    override fun onCreate() {
        super.onCreate()

        createProjectTimeNotificationChannel()
    }

    private fun createProjectTimeNotificationChannel() {
        val notificationChannel = NotificationChannel(
            ProjectTimeForegroundService.CHANNEL_ID,
            getString(R.string.title_project_time_channel),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(true)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}