package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.data.database.dao.RunDao
import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.data.database.entity.RunWithRaces
import kotlinx.coroutines.flow.Flow

class RunRepository(private val runDao: RunDao) {

    suspend fun insertRun(run: RunEntity): Long {
        return runDao.insertRun(run)
    }

    suspend fun updateRun(run: RunEntity) {
        runDao.updateRun(run)
    }

    suspend fun deleteRun(run: RunEntity) {
        runDao.deleteRun(run)
    }

    suspend fun getRunWithRaces(runId: Int): RunWithRaces? {
        return runDao.getRunWithRaces(runId)
    }

    fun getAllRuns(): Flow<List<RunEntity>> {
        return runDao.getAllRuns()
    }
}
