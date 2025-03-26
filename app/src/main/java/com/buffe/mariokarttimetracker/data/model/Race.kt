package com.buffe.mariokarttimetracker.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.buffe.mariokarttimetracker.ui.main.RaceTime

// Ein einzelnes Rennen innerhalb eines Runs
data class Race(
  //  val id:Int=0,
    val track: Track,         // Strecke, auf der gefahren wurde
    val raceTime: RaceTime,   // Gesamtzeit für das Rennen
    val bestLapTime: RaceTime? // Optional: Beste Rundenzeit
)


