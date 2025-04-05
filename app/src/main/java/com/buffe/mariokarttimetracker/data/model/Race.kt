package com.buffe.mariokarttimetracker.data.model


import com.buffe.mariokarttimetracker.ui.main.RaceTime
import java.io.Serializable


// Ein einzelnes Rennen innerhalb eines Runs

data class Race(
    val id: Long?,
    val track: Track, // Strecke, auf der gefahren wurde
    val raceTime: RaceTime,   // Gesamtzeit für das Rennen
    val bestLapTime: RaceTime? // Optional: Beste Rundenzeit
):Serializable


