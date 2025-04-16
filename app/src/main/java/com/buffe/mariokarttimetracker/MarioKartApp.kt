package com.buffe.mariokarttimetracker

import android.app.Application
import com.buffe.mariokarttimetracker.data.database.entity.MyObjectBox
import com.buffe.mariokarttimetracker.data.repository.RunRepository

import io.objectbox.BoxStore


class MarioKartApp : Application() {
    companion object {
        lateinit var boxStore: BoxStore
            private set
    }
    override fun onCreate() {
        super.onCreate()
// Lade die OpenCV-Bibliothek

        System.loadLibrary("opencv_java4")

        boxStore = MyObjectBox.builder()
            .androidContext(this).build()

        val runRepository=RunRepository()
        runRepository.initializeRuns()
    }
}


