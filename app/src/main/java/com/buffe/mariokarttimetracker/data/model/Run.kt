package com.buffe.mariokarttimetracker.data.model

import java.io.Serializable

// Ein kompletter Durchlauf aller 96 Strecken
data class Run(
    var id: Long?,
    val startTime: Long = System.currentTimeMillis(),
    val races: MutableList<Race> = mutableListOf<Race>(),
    var currentRaceIndex: Int = 1
):Serializable {
    // Startzeit wird automatisch gesetzt


    constructor() : this(null, System.currentTimeMillis(), mutableListOf())

    fun addRace(race: Race) {
        if (races.size < 96) {
            races.add(race)
        } else {
            throw IllegalStateException("Run ist bereits abgeschlossen.")
        }
    }

    fun incrementCurrentRaceIndexByOne() {
        currentRaceIndex++
    }

    fun calculateTotalTime(): RaceTime {
        val totalMillis = races.sumOf { it.raceTime.timeMillis ?: 0L }
        return RaceTime(totalMillis)
    }

    fun isCompleted(): Boolean {
        return races.size >= 96
    }
}
