package com.buffe.mariokarttimetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "run",
    foreignKeys = [ForeignKey(
        entity = TrackEntity::class,
        parentColumns = ["id"],
        childColumns = ["currentTrackId"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class RunEntity @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,  // Eindeutige ID für jedes Rennen
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0,

    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean = false,

    @ColumnInfo(name = "currentTrackId")
    var currentTrackId: Int? = null
)