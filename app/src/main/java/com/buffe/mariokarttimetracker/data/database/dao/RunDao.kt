package com.buffe.mariokarttimetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.data.database.entity.RunWithRaces
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Transaction
    @Query("SELECT * FROM run WHERE id = :runId")
    fun getRunWithRaces(runId: Int): RunWithRaces?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunEntity): Long

    @Update
    suspend fun updateRun(run: RunEntity)

    @Delete
    suspend fun deleteRun(run: RunEntity)

    @Query("SELECT * FROM run WHERE id = :runId LIMIT 1")
    suspend fun getRunById(runId: Int): RunEntity?

    @Query("SELECT * FROM run ORDER BY timestamp DESC")
    fun getAllRuns(): Flow<List<RunEntity>>

    @Query("SELECT * FROM run WHERE isFinished = 0 LIMIT 1")
    suspend fun getCurrentRun(): RunEntity?


}