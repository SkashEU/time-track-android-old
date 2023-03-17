package com.skash.timetrack.feature.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.skash.timetrack.R
import com.skash.timetrack.core.exception.MissingTimerActionException
import java.util.Timer
import java.util.TimerTask

class ProjectTimerService : Service() {

    companion object {
        // Channel ID for notifications
        const val CHANNEL_ID = "project_time_service_channel"
        const val NOTIFICATION_ID = 999

        // Service Actions
        const val START = "START"
        const val STOP = "STOP"
        const val GET_STATUS = "GET_STATUS"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"

        // Intent Extras
        const val TIMER_ACTION = "TIMER_ACTION"
        const val TIME_ELAPSED = "TIME_ELAPSED"
        const val IS_TIMER_RUNNING = "IS_TIMER_RUNNING"
        const val IS_TIMER_FINISHED = "IS_TIMER_FINISHED"

        // Intent Actions
        const val TIMER_TICK = "TIMER_TICK"
        const val TIMER_STATUS = "TIMER_STATUS"

        fun formatElapsedTime(hours: Int, minutes: Int, seconds: Int): String {
            val hours = "%02d".format(hours)
            val minutes = "%02d".format(minutes)
            val seconds = "%02d".format(seconds)

            return "$hours:$minutes:$seconds"
        }
    }

    private var timeElapsed: Int = 0
    private var isTimerRunning = false

    private var foregroundNotificationUpdateTimer = Timer()
    private var stopwatchTimer = Timer()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.getStringExtra(TIMER_ACTION)) {
            START -> startTimer()
            STOP -> stopTimer()
            GET_STATUS -> postStatus()
            MOVE_TO_FOREGROUND -> moveToForeground()
            MOVE_TO_BACKGROUND -> moveToBackground()
            else -> throw MissingTimerActionException("Cant start service without a action!")
        }

        return START_STICKY
    }

    private fun startTimer() {
        isTimerRunning = true
        postStatus()

        stopwatchTimer = Timer()

        cyclicallyBroadcastElapsedTime()
    }

    private fun stopTimer() {
        stopwatchTimer.cancel()
        foregroundNotificationUpdateTimer.cancel()
        isTimerRunning = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        postStatus(true)
        timeElapsed = 0
    }

    private fun moveToForeground() {
        if (isTimerRunning.not()) {
            Log.d(javaClass.name, "Ignoring moveToForeground request. Timer is not running...")
            return
        }

        startForeground(NOTIFICATION_ID, createNotification())
        foregroundNotificationUpdateTimer = Timer()
        cyclicallyPostForegroundNotification()
    }

    private fun moveToBackground() {
        if (isTimerRunning.not()) {
            Log.d(javaClass.name, "Ignoring moveToBackground request. Timer is not running...")
            return
        }
        foregroundNotificationUpdateTimer.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun postStatus(isFinished: Boolean = false) {
        val statusIntent = Intent().apply {
            action = TIMER_STATUS
            putExtra(IS_TIMER_RUNNING, isTimerRunning)
            putExtra(TIME_ELAPSED, timeElapsed)
            putExtra(IS_TIMER_FINISHED, isFinished)
        }

        LocalBroadcastManager.getInstance(
            this
        ).sendBroadcast(statusIntent)
    }

    private fun cyclicallyBroadcastElapsedTime() {
        stopwatchTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeElapsed++
                val timerIntent = Intent().apply {
                    action = TIMER_TICK
                    putExtra(TIME_ELAPSED, timeElapsed)
                }

                LocalBroadcastManager.getInstance(
                    this@ProjectTimerService
                ).sendBroadcast(timerIntent)
            }

        }, 0, 1000)
    }

    private fun cyclicallyPostForegroundNotification() {
        foregroundNotificationUpdateTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Log.d(javaClass.name, "Posting foreground notification")
                postNotification()
            }

        }, 0, 1000)
    }

    private fun postNotification() {
        getSystemService(NotificationManager::class.java).notify(
            NOTIFICATION_ID,
            createNotification()
        )
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.title_timer_notification))
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.icn_timer)
            .setContentText(formatElapsedTime())
            .build()

    }

    private fun formatElapsedTime(): String {
        val hours = timeElapsed / 60 / 60
        val minutes = timeElapsed / 60
        val seconds = timeElapsed % 60

        return formatElapsedTime(hours, minutes, seconds)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}