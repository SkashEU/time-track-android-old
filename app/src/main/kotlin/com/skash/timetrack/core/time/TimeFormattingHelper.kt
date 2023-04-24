package com.skash.timetrack.core.time

fun formatElapsedTime(hours: Int, minutes: Int, seconds: Int): String {
    val hours = "%02d".format(hours)
    val minutes = "%02d".format(minutes)
    val seconds = "%02d".format(seconds)

    return "$hours:$minutes:$seconds"
}

fun Int.secondsToHoursMinutesSeconds(): Triple<Int, Int, Int> {
    val hours: Int = (this / 60) / 60
    val minutes: Int = (this / 60)
    val seconds: Int = (this % 60)

    return Triple(hours, minutes, seconds)
}
