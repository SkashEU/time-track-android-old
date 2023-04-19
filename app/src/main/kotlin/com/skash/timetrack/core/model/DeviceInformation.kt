package com.skash.timetrack.core.model

import android.os.Build

object DeviceInformation {
    val manufacturer: String = Build.MANUFACTURER
    val model: String = Build.MODEL
    val osVersion: String = Build.VERSION.SDK_INT.toString()
}