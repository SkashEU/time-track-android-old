package com.skash.timetrack.core.helper.date

import java.util.Calendar
import java.util.Date

fun Date.minusSeconds(seconds: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    calendar.add(Calendar.SECOND, seconds.unaryMinus())

    return calendar.time
}