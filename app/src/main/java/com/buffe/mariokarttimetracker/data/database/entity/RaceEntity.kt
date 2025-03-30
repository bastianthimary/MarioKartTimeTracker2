package com.buffe.mariokarttimetracker.data.database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class RaceEntity(
   @Id var id: Long = 0,  // Eindeutige ID für jedes Rennen
    var raceTimeInMillis: Long=0,   // Zeit als Long (für Berechnungen optimiert)
    var bestLapTimeInMillis:Long?=null,

){
    lateinit var track: ToOne<TrackEntity>
   lateinit var run:ToOne<RunEntity>
}
