package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.data.database.dao.RaceDao
import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity


class RaceRepository(private val raceDao: RaceDao) {

    suspend fun insertRace(race: RaceEntity) {
        return raceDao.insertRace(race)
    }

    suspend fun updateRace(race: RaceEntity) {
        raceDao.updateRace(race)
    }

    suspend fun deleteRace(race: RaceEntity) {
        raceDao.deleteRace(race)
    }

    suspend fun getBestTimeUntil(trackId: Long): Long?{
       return raceDao.getBestTimeUntil(trackId)
    }

    suspend fun getAverageTimeUntil(trackId: Long): Long?{
        return raceDao.getAverageTimeUntil(trackId)
    }


}