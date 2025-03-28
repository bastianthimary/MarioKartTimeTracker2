package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.data.database.dao.RunDao
import com.buffe.mariokarttimetracker.data.database.entity.RunWithRaces
import com.buffe.mariokarttimetracker.data.mapper.RunMapper
import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.ui.main.Run
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RunRepository(private val runDao: RunDao,private val raceRepository: RaceRepository,private val trackRepository: TrackRepository) {

    suspend fun insertRun(run: Run): Long {
        return runDao.insertRun(RunMapper.toEntity(run))
    }

    suspend fun updateRun(run: Run) {
        runDao.updateRun(RunMapper.toEntity(run))
    }

    suspend fun deleteRun(run: Run) {
        runDao.deleteRun(RunMapper.toEntity(run))
    }

    suspend fun getRunWithRaces(runId: Int): Run? {
        return RunMapper.toDomain(runDao.getRunWithRaces(runId),listOf<Race>() )
    }

    fun getAllRuns(): List<Run> {
        // Zuerst holen wir uns alle Tracks (dieser Aufruf ist suspend und sollte im IO-Thread erfolgen)
        val trackMap = trackRepository.getAllTracks().associateBy { it.id }
        // Dann mappen wir den Flow
        return runDao.getAllRunsWithRaces().map { runWithoutRaces ->
            RunMapper.toDomain(runWithoutRaces,listOf<Race>() )
        }
    }
}


