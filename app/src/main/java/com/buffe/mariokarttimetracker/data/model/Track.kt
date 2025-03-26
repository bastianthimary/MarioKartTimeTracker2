package com.buffe.mariokarttimetracker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Strecke mit fester ID und Name
@Entity(tableName = "track")
data class Track(
    @PrimaryKey
    @ColumnInfo(name="id")
    val id: Int,  // Eindeutige ID für die Strecke
    @ColumnInfo(name = "name")
    val name: String    // Name der Strecke
)