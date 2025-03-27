package com.buffe.mariokarttimetracker.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RunWithRaces(
    @Embedded val run: RunEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "runId"
    )
    val races: List<RaceEntity>
)
