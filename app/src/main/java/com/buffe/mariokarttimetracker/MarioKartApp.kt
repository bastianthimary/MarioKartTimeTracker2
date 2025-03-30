package com.buffe.mariokarttimetracker

import android.app.Application
import com.buffe.mariokarttimetracker.data.database.entity.MyObjectBox

import com.buffe.mariokarttimetracker.data.repository.TrackRepository
import io.objectbox.BoxStore


class MarioKartApp : Application() {
    companion object {
        lateinit var boxStore: BoxStore
            private set
    }
    override fun onCreate() {
        super.onCreate()

        boxStore = MyObjectBox.builder()
            .androidContext(this).build()
        val trackRepository = TrackRepository()
       trackRepository.initializeTracks()
    }
}


