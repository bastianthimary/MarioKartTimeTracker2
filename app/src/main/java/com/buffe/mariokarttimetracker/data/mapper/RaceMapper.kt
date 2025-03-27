package com.buffe.mariokarttimetracker.data.mapper

import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity
import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.ui.main.RaceTime

object RaceMapper {
    fun toDomain(entity: RaceEntity, track: Track): Race {
        return Race(
            id = entity.id,
            track = track,
            raceTime = RaceTime(entity.raceTimeInMillis),
            bestLapTime = entity.bestLapTimeInMillis?.let {RaceTime(entity.bestLapTimeInMillis) }
        )
    }

    fun toEntity(race: Race, runId: Int): RaceEntity {
        return RaceEntity(
            id = race.id?:0,
            trackId = race.track.id,
            raceTimeInMillis = race.raceTime.timeMillis,
            bestLapTimeInMillis = race.bestLapTime?.let { race.bestLapTime.timeMillis },
            runId = runId
        )
    }
}