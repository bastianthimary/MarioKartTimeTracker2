package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.data.TrackData
import com.buffe.mariokarttimetracker.data.database.dao.TrackDao
import com.buffe.mariokarttimetracker.data.mapper.TrackMapper
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
        return trackDao.getAllTracks().map { TrackMapper.toDomain(it) }
    }

    fun getTrackById(id: Int): Track?{
        return trackDao.getTrackById(id)?.let { TrackMapper.toDomain(it) }
    }
}