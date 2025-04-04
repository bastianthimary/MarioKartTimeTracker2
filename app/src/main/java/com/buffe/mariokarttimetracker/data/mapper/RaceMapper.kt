package com.buffe.mariokarttimetracker.data.mapper

import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity
import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.ui.main.RaceTime
import com.buffe.mariokarttimetracker.ui.main.Run

object RaceMapper {
    fun toDomain(entity: RaceEntity): Race {
        return Race(
            id = entity.id,
            track = Track.getById(entity.trackId),
            raceTime = RaceTime(entity.raceTimeInMillis),
            bestLapTime = entity.bestLapTimeInMillis?.let { RaceTime(it) }
        )
    }

    fun toEntity(race: Race, runEntity: RunEntity): RaceEntity {
        return RaceEntity(
            id = race.id ?: 0,
            raceTimeInMillis = race.raceTime.timeMillis,
            bestLapTimeInMillis = race.bestLapTime?.timeMillis,
            trackId = race.track.id
        ).apply {
            run.target = runEntity
        }

    }
}