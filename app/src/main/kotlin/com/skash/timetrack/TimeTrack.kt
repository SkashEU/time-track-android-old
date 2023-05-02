package com.skash.timetrack

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.skash.timetrack.core.cache.seeder.ProjectColorSeeder
import com.skash.timetrack.core.helper.context.reloadTasks
import com.skash.timetrack.core.helper.context.reloadWorkTime
import com.skash.timetrack.core.helper.sharedprefs.SHARED_PREFS_SELECTED_WORKSPACE_ID
import com.skash.timetrack.core.helper.sharedprefs.SHARED_PREFS_SELF_USER_ID
import com.skash.timetrack.feature.service.TaskTimerService
import com.skash.timetrack.feature.service.WorkTimeTimerService
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class TimeTrack : Application(), SharedPreferences.OnSharedPreferenceChangeListener {

    val sharedPreferences by lazy {
        createEncryptedSharedPrefsInstance()
    }

    companion object {
        private const val SHARED_PREFS_NAME = "shared_prefs"
    }

    override fun onCreate() {
        super.onCreate()

        setupRealm()
        createProjectTimeNotificationChannel()
        createWorkTimeTimeNotificationChannel()

        // Used to reload tasks cache after changing the selected workspace
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun createEncryptedSharedPrefsInstance(): SharedPreferences {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            this,
            SHARED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun setupRealm() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .allowQueriesOnUiThread(false)
            .allowWritesOnUiThread(false)
            .initialData(ProjectColorSeeder())
            .build()

        Realm.setDefaultConfiguration(config)
    }

    private fun createProjectTimeNotificationChannel() {
        val notificationChannel = NotificationChannel(
            TaskTimerService.CHANNEL_ID,
            getString(R.string.title_project_time_channel),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(true)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun createWorkTimeTimeNotificationChannel() {
        val notificationChannel = NotificationChannel(
            WorkTimeTimerService.CHANNEL_ID,
            getString(R.string.title_work_time_channel),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(true)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

        when (key) {
            SHARED_PREFS_SELECTED_WORKSPACE_ID -> reloadTasks()
            SHARED_PREFS_SELF_USER_ID -> reloadWorkTime()
            else -> Log.d(javaClass.name, "Ignoring changes of key $key")
        }
    }
}