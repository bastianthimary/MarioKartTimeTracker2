package com.buffe.mariokarttimetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity



@Dao
interface RaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRace(race: RaceEntity)

    @Update
    suspend fun updateRace(race: RaceEntity)

    @Delete
    suspend fun deleteRace(race: RaceEntity)

    @Query("SELECT * FROM race WHERE trackId = :trackId ORDER BY id DESC")
    suspend fun getRacesByTrack(trackId: Long): List<RaceEntity>

    @Query("""
        SELECT MIN(timeInMillis) 
        FROM race 
        WHERE trackId = :trackId 
    """)
    suspend fun getBestTimeUntil(trackId: Long): Long?

    @Query("""
        SELECT AVG(timeInMillis) 
        FROM race 
        WHERE trackId = :trackId 
    """)
    suspend fun getAverageTimeUntil(trackId: Long): Long?

}