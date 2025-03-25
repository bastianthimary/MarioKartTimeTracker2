package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.data.model.Track

interface TrackRepository {
    fun getAllTracks(): List<Track>
    fun getTrackById(id: Int): Track?
}