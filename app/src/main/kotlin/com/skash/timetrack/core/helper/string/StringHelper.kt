package com.skash.timetrack.core.helper.string

import android.util.Base64

fun String.decode(): String {
    return Base64.decode(this, Base64.NO_WRAP).toString()
}

fun String.encode(): String {
    return Base64.encodeToString(this.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
}