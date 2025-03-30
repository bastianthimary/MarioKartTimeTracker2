package com.buffe.mariokarttimetracker.data.mapper

import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity
import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.ui.main.RaceTime
import com.buffe.mariokarttimetracker.ui.main.Run

object RaceMapper {
    fun toDomain(entity: RaceEntity): Race {
        return Race(
            id = entity.id,
            track = TrackMapper.toDomain(entity.track.target),
            raceTime = RaceTime(entity.raceTimeInMillis),
            bestLapTime = entity.bestLapTimeInMillis?.let {RaceTime(entity.bestLapTimeInMillis!!) }
        )
    }

    fun toEntity(race: Race, run: Run?): RaceEntity {
        var raceEntity=RaceEntity(
            id = race.id?:0,
            raceTimeInMillis = race.raceTime.timeMillis,
            bestLapTimeInMillis = race.bestLapTime?.let { race.bestLapTime.timeMillis },
        )
        raceEntity.track.setAndPutTarget(TrackMapper.toEntity(race.track))
        run?.let { raceEntity.run.setAndPutTarget(RunMapper.toEntity(run))}
        return raceEntity
    }
}