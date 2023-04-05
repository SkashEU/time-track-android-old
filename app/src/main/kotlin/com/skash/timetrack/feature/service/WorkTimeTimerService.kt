package com.skash.timetrack.feature.service

import android.app.Notification
import androidx.core.app.NotificationCompat
import com.skash.timetrack.R

class WorkTimeTimerService : TimerService() {

    companion object {
        // Channel ID for notifications
        const val CHANNEL_ID = "work_time_service_channel"
        const val NOTIFICATION_ID = 998

        // Intent Actions
        const val TIMER_TICK = "WORK_TIME_TIMER_TICK"
        const val TIMER_STATUS = "WORK_TIME_TIMER_STATUS"
    }

    override fun getChannelID(): String {
        return CHANNEL_ID
    }

    override fun getNotificationID(): Int {
        return NOTIFICATION_ID
    }

    override fun getStatusAction(): String {
        return TIMER_STATUS
    }

    override fun getTickAction(): String {
        return TIMER_TICK
    }

    override fun createNotification(): Notification {
        return NotificationCompat.Builder(this, getChannelID())
            .setContentTitle(getString(R.string.title_work_time_timer_notification))
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.icn_timer)
            .setContentText(formatElapsedTime())
            .build()
    }
}