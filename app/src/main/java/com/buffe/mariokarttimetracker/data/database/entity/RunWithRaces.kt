package com.buffe.mariokarttimetracker.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RunWithRaces(

    val races: List<RaceEntity>
)
