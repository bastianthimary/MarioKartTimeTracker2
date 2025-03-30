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

    fun getBestTimeUntil(trackId: Int): Long {

        return raceBox.query().build().property(RaceEntity_.raceTimeInMillis).min()
    }

    fun getAverageTimeUntil(trackId: Int): Long {
        return  raceBox.query().build().property(RaceEntity_.raceTimeInMillis).avgLong()
    }


}