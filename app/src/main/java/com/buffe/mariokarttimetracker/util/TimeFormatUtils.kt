package com.buffe.mariokarttimetracker.util

object TimeFormatUtils {
    fun formatTime(timeMillis: Long): String {
        val minutes = timeMillis / 60000
        val seconds = (timeMillis % 60000) / 1000
        val millis = timeMillis % 1000
        return String.format("%d:%02d.%03d", minutes, seconds, millis)
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
}