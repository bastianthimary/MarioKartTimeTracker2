package com.buffe.mariokarttimetracker.data.database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity
data class TrackEntity(
    @Id(assignable = true)
    var id: Long = 0,
    var name: String = ""   // Name der Strecke
)
