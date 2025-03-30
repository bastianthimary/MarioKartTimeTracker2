package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.MarioKartApp
import com.buffe.mariokarttimetracker.data.TrackData
import com.buffe.mariokarttimetracker.data.database.entity.TrackEntity
import com.buffe.mariokarttimetracker.data.mapper.TrackMapper
import com.buffe.mariokarttimetracker.data.model.Track
import io.objectbox.Box
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackRepository {
    private val trackBox: Box<TrackEntity> = MarioKartApp.boxStore.boxFor(TrackEntity::class.java)

    fun initializeTracks() {
        CoroutineScope(Dispatchers.IO).launch {
            if (trackBox.all.isEmpty()) {
                trackBox.put(TrackData.tracks)
            }
        }
    }

    fun getAllTracks(): List<Track> {
        return trackBox.all.map { TrackMapper.toDomain(it) }
    }

    fun getTrackById(id: Long): Track{
        return trackBox.get(id).let { TrackMapper.toDomain(it) }
    }
}