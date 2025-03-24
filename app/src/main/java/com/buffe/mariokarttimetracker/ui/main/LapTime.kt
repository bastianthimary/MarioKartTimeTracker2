package com.buffe.mariokarttimetracker.ui.main

data class LapTime(val lapNumber: Int, val timeInMillis: Long) {

    fun getFormattedLapTime(): String {
        val minutes = (timeInMillis / 60000).toInt()
        val seconds = ((timeInMillis % 60000) / 1000).toInt()
        val milliseconds = (timeInMillis % 1000).toInt()
        return String.format("%d:%02d.%03d", minutes, seconds, milliseconds)
    }
}
