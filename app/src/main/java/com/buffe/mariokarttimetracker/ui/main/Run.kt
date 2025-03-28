package com.buffe.mariokarttimetracker.ui.main

import com.buffe.mariokarttimetracker.data.model.Race

// Ein kompletter Durchlauf aller 96 Strecken
data class Run(
    val id:Int?,
    val startTime: Long = System.currentTimeMillis(),
    val currentTrackId: Int?, // ID der aktuellen Strecke (null, wenn der Run abgeschlossen ist)
    val races: MutableList<Race> = mutableListOf<Race>()
    ){


    // Startzeit wird automatisch gesetzt
     var currentRaceIndex = 0

    constructor() : this(null, System.currentTimeMillis(), null, mutableListOf())
    fun addRace(race: Race) {
        if (races.size < 96) {
            races.add(race)
        } else {
            throw IllegalStateException("Run ist bereits abgeschlossen.")
        }
    }
    fun incrementCurrentRaceIndexByOne(){
        currentRaceIndex++
    }
    fun calculateTotalTime(): RaceTime {
        val totalMillis = races.sumOf { it.raceTime?.timeMillis ?: 0L }
        return RaceTime(totalMillis)
    }

    fun isCompleted(): Boolean {
        return races.size >= 96
    }
}
