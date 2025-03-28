package com.buffe.mariokarttimetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRace(race: RaceEntity):Long

    @Update
    suspend fun updateRace(race: RaceEntity)

    @Delete
    suspend fun deleteRace(race: RaceEntity): Int

    @Query("SELECT * FROM race WHERE trackId = :trackId")
    fun getRacesByTrack(trackId: Int): Flow<List<RaceEntity>>

    @Query("SELECT MIN(timeInMillis) FROM race WHERE trackId = :trackId")
    fun getBestTimeUntil(trackId: Int): Flow<Long>

    @Query("SELECT AVG(timeInMillis) FROM race WHERE trackId = :trackId")
    fun getAverageTimeUntil(trackId: Int): Flow<Long>
}
