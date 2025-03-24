package com.buffe.mariokarttimetracker.ui.main

data class Run(val lapTimes: List<LapTime>) {
    fun totalTime(): Long = lapTimes.sumOf { it.timeInMillis }
}
