package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.data.database.TrackDao
import com.buffe.mariokarttimetracker.data.database.TrackData
import com.buffe.mariokarttimetracker.data.model.Track
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

    fun getAllTracks(): List<Track> {
        return trackDao.getAllTracks()
    }

    fun getTrackById(id: Int): Track?{
        return trackDao.getTrackById(id)
    }
}