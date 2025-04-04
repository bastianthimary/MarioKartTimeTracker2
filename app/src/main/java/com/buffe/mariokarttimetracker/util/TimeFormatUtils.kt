package com.buffe.mariokarttimetracker.util

object TimeFormatUtils {
    fun formatTime(timeMillis: Long): String {
        val minutes = timeMillis / 60000
        val seconds = (timeMillis % 60000) / 1000
        val millis = timeMillis % 1000
        return String.format("%d:%02d.%03d", minutes, seconds, millis)
    }
    fun hourFormatTime(timeMillis: Long): String {
        val hours = timeMillis / 3_600_000
        val remainingMillis = timeMillis % 3_600_000
        val minutes = remainingMillis / 60_000
        val remainingAfterMinutes = remainingMillis % 60_000
        val seconds = remainingAfterMinutes / 1_000
        val millis = remainingAfterMinutes % 1_000

        return String.format(
            "%02d:%02d:%02d.%03d",
            hours,
            minutes,
            seconds,
            millis
        )
    }

    fun parseTime(timeString: String): Long {
        val regex = Regex("^(\\d+):(\\d{2})\\.(\\d{3})$")
        val matchResult = regex.matchEntire(timeString) ?: throw IllegalArgumentException("Invalid time format: $timeString")
        val (minutes, seconds, millis) = matchResult.destructured
        return (minutes.toLong() * 60000) + (seconds.toLong() * 1000) + millis.toLong()
    }

    fun isValidTimeFormat(input: String): Boolean {
        return input.matches(Regex("^\\d+:\\d{2}\\.\\d{3}$"))
    }
    fun formatTimeInput(digits: String): String {
        if (digits.isEmpty()) return ""
        return when (digits.length) {
            in 1..3 -> "0:00." + digits.padStart(3, '0')
            in 4..5 -> "0:" + digits.substring(0, digits.length - 3).padStart(2, '0') + "." + digits.takeLast(3)
            else -> {
                val minutes = digits.substring(0, digits.length - 5)
                val seconds = digits.substring(digits.length - 5, digits.length - 3)
                val millis = digits.takeLast(3)
                "$minutes:$seconds.$millis"
            }
        }
    }
}