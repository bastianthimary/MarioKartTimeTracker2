package com.buffe.mariokarttimetracker.ui.main

import com.buffe.mariokarttimetracker.data.model.Race

// Ein kompletter Durchlauf aller 96 Strecken
data class Run(
    val runId: Int,           // Eindeutige ID für den Run
    val races: MutableList<Race>, // Alle Rennen des Runs
    var currentRaceIndex: Int, // Speichert, welches Rennen gerade an der Reihe ist
    val startTime: Long = System.currentTimeMillis() // Startzeit wird automatisch gesetzt
) {
    fun addRace(race: Race) {
        if (races.size < 96) {
            races.add(race)
            currentRaceIndex = races.size
        } else {
            throw IllegalStateException("Run ist bereits abgeschlossen.")
        }
    }
    fun calculateTotalTime(): RaceTime {
        val totalMillis = races.sumOf { it.raceTime?.timeMillis ?: 0L }
        return RaceTime(totalMillis)
    }

    fun isCompleted(): Boolean {
        return races.size >= 96
    }
}
