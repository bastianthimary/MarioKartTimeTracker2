package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.data.database.dao.TrackDao
import com.buffe.mariokarttimetracker.data.TrackData
import com.buffe.mariokarttimetracker.data.database.entity.TrackEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackRepository(private val trackDao: TrackDao) {

    fun initializeTracks() {
        CoroutineScope(Dispatchers.IO).launch {
            val existingTracks = trackDao.getAllTracks()
            if (existingTracks.isEmpty()) {
                trackDao.insertAll(TrackData.tracks)
            }
        }
    }

    fun getAllTracks(): List<TrackEntity> {
        return trackDao.getAllTracks()
    }

    fun getTrackById(id: Int): TrackEntity?{
        return trackDao.getTrackById(id)
    }
}