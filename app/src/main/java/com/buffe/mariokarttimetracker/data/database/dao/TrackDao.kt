package com.buffe.mariokarttimetracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buffe.mariokarttimetracker.data.database.entity.TrackEntity


@Dao
interface TrackDao {
    @Query("SELECT * FROM track")
    fun getAllTracks(): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tracks: List<TrackEntity>)

    @Query("SELECT COUNT(*) FROM track")
    fun getTrackCount(): Int

    @Query("SELECT * FROM track WHERE id= :id")
    fun getTrackById(id:Int):TrackEntity?

}