package com.buffe.mariokarttimetracker.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "race",
foreignKeys = [ForeignKey(
entity = TrackEntity::class,
parentColumns = ["id"],
childColumns = ["trackId"],
onDelete = ForeignKey.CASCADE
)],
indices = [Index(value = ["trackId"])]
)
data class RaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,  // Eindeutige ID für jedes Rennen
    val trackId: Int, // Referenz zur Strecke (ForeignKey)
    @ColumnInfo(name = "timeInMillis")
    val timeInMillis: Long,   // Zeit als Long (für Berechnungen optimiert)
    val timestamp: Long,       // Zeitpunkt des Rennens (Millisekunden seit 1970)
    val runId:Int
)
