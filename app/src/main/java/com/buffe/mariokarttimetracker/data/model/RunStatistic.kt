package com.buffe.mariokarttimetracker.data.model

import java.io.Serializable

data class RunStatistic(
    var recordCount: Int = 0,
    val trackPlacements: MutableMap<Int, Int> = mutableMapOf()
) : Serializable {

    fun updateTrackPlacement(trackId: Int, newPlacement: Int) {
        val current = trackPlacements[trackId]
        if (current == null || newPlacement < current) {
            trackPlacements[trackId] = newPlacement
        }
    }

    fun addRecord() {
        recordCount++
    }
}
