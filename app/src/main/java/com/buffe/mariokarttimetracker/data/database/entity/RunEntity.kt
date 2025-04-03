package com.buffe.mariokarttimetracker.data.database.entity

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany


@Entity
data class RunEntity(
    @Id
    var id: Long = 0,  // Eindeutige ID für jedes Rennen
    var startTime: Long = 0,
    var finished: Boolean = false,
    var currentRaceIndex:Int=0
){
    @Backlink(to="run")
   lateinit var races: ToMany<RaceEntity>
}