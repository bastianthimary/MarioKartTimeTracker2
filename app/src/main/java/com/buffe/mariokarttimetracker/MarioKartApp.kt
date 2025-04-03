package com.buffe.mariokarttimetracker

import android.app.Application
import com.buffe.mariokarttimetracker.data.database.entity.MyObjectBox

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
    }
}


