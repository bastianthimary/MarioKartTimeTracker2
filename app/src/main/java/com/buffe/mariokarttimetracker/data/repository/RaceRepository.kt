package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.MarioKartApp
import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity
import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity_
import com.buffe.mariokarttimetracker.data.mapper.RaceMapper
import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.ui.main.Run
import io.objectbox.Box


class RaceRepository {
    private val raceBox: Box<RaceEntity> = MarioKartApp.boxStore.boxFor(RaceEntity::class.java)
    fun insertRace(race: Race, run: Run) {
        raceBox.put(RaceMapper.toEntity(race, run))
    }

    fun updateRace(race: Race, run: Run) {
        raceBox.put(RaceMapper.toEntity(race, run))
    }

    fun getBestRaceTimeOfTrack(trackId: Long): Long {
        return getAllRacesByTrack(trackId).minOfOrNull { it.raceTimeInMillis } ?: 0L
    }

    fun getAverageRaceTimeOfTrack(trackId: Long): Long {
        if (getAllRacesByTrack(trackId).isEmpty()) {
            return 0L
        }
        return raceBox.query(RaceEntity_.trackId.equal(trackId)).build()
            .property(RaceEntity_.raceTimeInMillis).avgLong()
    }

    fun getAllRacesByTrack(trackId: Long): List<RaceEntity> {
        val query = raceBox.query(RaceEntity_.trackId.equal(trackId)).build()
        val result = query.find()
        query.close()
        return result
    }

    fun getAllRacesTillTrack(runId: Long, trackId: Long): List<RaceEntity> {
        return raceBox.query(
            RaceEntity_.trackId.lessOrEqual(trackId)
                .and(RaceEntity_.runId.equal(runId))
        ).build().find()
    }

}