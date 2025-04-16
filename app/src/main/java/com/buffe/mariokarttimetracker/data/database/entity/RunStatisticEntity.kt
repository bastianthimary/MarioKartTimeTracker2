package com.buffe.mariokarttimetracker.data.database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class RunStatisticEntity(
    @Id(assignable = true) var id: Long = 1, //Fest Definiert - es darf nur eine Geben
    var recordCount: Int = 0,     // Anzahl der gebrochenen Rekorde
    var placementsJson: String = ""   // JSON-String, der eine Zuordnung von Track-ID -> Platzierung abbildet
)
