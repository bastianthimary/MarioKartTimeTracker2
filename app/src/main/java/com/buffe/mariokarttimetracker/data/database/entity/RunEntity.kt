package com.buffe.mariokarttimetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "run",
    foreignKeys = [ForeignKey(
        entity = TrackEntity::class,
        parentColumns = ["id"],
        childColumns = ["currentTrackId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["currentTrackId"])]
)
data class RunEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,  // Eindeutige ID für jedes Rennen
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0,

    @ColumnInfo(name = "finished")
    var finished: Boolean = false,

    @ColumnInfo(name = "currentTrackId")
    var currentTrackId: Int? = null
){
    // Expliziter no-arg Konstruktor, der von Room ignoriert wird.
    @Ignore
    constructor() : this(0, 0, false, null)
}