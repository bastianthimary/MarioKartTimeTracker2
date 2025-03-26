package com.buffe.mariokarttimetracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buffe.mariokarttimetracker.data.model.Track

@Dao
interface TrackDao {
    @Query("SELECT * FROM track")
    fun getAllTracks(): List<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tracks: List<Track>)

    @Query("SELECT COUNT(*) FROM track")
    fun getTrackCount(): Int

    @Query("SELECT * FROM track WHERE id= :id")
    fun getTrackById(id:Int):Track?

}