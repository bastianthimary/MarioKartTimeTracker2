package com.buffe.mariokarttimetracker.ui.main

import com.buffe.mariokarttimetracker.data.model.Race

// Ein kompletter Durchlauf aller 96 Strecken
data class Run(val startTime: Long = System.currentTimeMillis()){// Startzeit wird automatisch gesetzt
    val races= mutableListOf<Race>() // Alle Rennen des Runs
    fun addRace(race: Race) {
        if (races.size < 96) {
            races.add(race)
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
