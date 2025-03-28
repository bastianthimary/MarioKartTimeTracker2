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
    suspend fun getRunWithRaces(runId: Int): RunEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunEntity): Long

    @Update
    suspend fun updateRun(run: RunEntity)

    @Delete
    suspend fun deleteRun(run: RunEntity): Int

    @Query("SELECT * FROM run WHERE id = :runId LIMIT 1")
     fun getRunById(runId: Int): RunEntity?

    @Transaction
    @Query("SELECT * FROM run")
     fun getAllRunsWithRaces(): List<RunEntity>

    @Query("SELECT * FROM run WHERE finished = 0 ")
     fun getCurrentRun(): Flow<List<RunEntity>>


}