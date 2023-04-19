package com.skash.timetrack.core.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(javaClass.name, "Received push: ${message.from}")
    }

    override fun onNewToken(token: String) {
        Log.d(javaClass.name, "new Push Token got generated")
    }
}