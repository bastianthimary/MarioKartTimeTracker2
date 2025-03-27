package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.data.database.dao.RaceDao
import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity
import com.buffe.mariokarttimetracker.data.mapper.RaceMapper
import com.buffe.mariokarttimetracker.data.model.Race


class RaceRepository(private val raceDao: RaceDao) {

    suspend fun insertRace(race: Race, runId: Int) {
        val entity = RaceMapper.toEntity(race, runId)
         raceDao.insertRace(entity)
    }

    suspend fun updateRace(race: Race,runId: Int) {
        raceDao.updateRace(RaceMapper.toEntity(race,runId))
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