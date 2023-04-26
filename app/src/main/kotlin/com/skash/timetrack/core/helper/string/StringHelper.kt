package com.skash.timetrack.core.helper.string

import android.util.Base64
import java.util.UUID

fun String.decode(): String {
    return Base64.decode(this, Base64.NO_WRAP).toString()
}

fun String.encode(): String {
    return Base64.encodeToString(this.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
}

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("""[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}""")
    return emailRegex.matches(this)
}

fun String.toUUID(): UUID? {
    return try {
        UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}