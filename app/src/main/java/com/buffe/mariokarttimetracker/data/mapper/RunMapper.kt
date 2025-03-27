package com.buffe.mariokarttimetracker.data.mapper

import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.data.database.entity.RunWithRaces
import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.ui.main.Run

object RunMapper {
    fun toDomain(runEntity: RunEntity, races: List<Race>): Run {
        return Run(
            id = runEntity.id,
            startTime = runEntity.timestamp,
            currentTrackId = runEntity.currentTrackId,
            races = races.toMutableList()
        )
    }

    fun toEntity(run: Run): RunEntity {
        return RunEntity(
            id = run.id?:0,
            timestamp = run.startTime,
            finished = run.isCompleted(),
            currentTrackId = run.currentTrackId,
        )
    }
    fun fromRunWithRaces(runWithRaces: RunWithRaces, trackMap: Map<Int, Track>): Run {
        return Run(
            id = runWithRaces.run.id,
            startTime = runWithRaces.run.timestamp,
            currentTrackId = runWithRaces.run.currentTrackId,
            races = runWithRaces.races.map { raceEntity ->
                // Hole das zugehörige Track-Domain-Objekt aus der Map
                val track = trackMap[raceEntity.trackId] ?: error("Missing track for id ${raceEntity.trackId}")
                RaceMapper.toDomain(raceEntity, track)
            }.toMutableList() // Umwandeln in MutableList, falls benötigt
        )
    }
}