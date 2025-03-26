package com.buffe.mariokarttimetracker

import android.app.Application
import com.buffe.mariokarttimetracker.data.database.AppDatabase
import com.buffe.mariokarttimetracker.data.repository.TrackRepository


class MarioKartApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val database = AppDatabase.getDatabase(this)
        val trackRepository = TrackRepository(database.trackDao())
       trackRepository.initializeTracks()
    }
}