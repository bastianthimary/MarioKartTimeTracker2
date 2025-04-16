package com.buffe.mariokarttimetracker.data.manager


import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.RaceTime
import com.buffe.mariokarttimetracker.data.model.Run
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.data.repository.RunRepository

class RunManager  {
    private lateinit var currentRun: Run
    private val runRepository = RunRepository()
    private val MAX_NUMBER_OF_RACES=96

    fun startNewRun(): Run {
        currentRun = Run()
        return currentRun
    }
    fun getCurrentRun(): Run {
        return currentRun
    }
    fun getCurrentTrack(): Track {
        return Track.getById(currentRun.currentRaceIndex)
    }

    fun addCurrentRace(raceTime: RaceTime) {
        currentRun.addRace(Race(0, getCurrentTrack(), raceTime, null))
        currentRun.incrementCurrentRaceIndexByOne()
        runRepository.updateRun(currentRun)
    }

    fun moveToNextRace() {
        if(currentRun.currentRaceIndex<MAX_NUMBER_OF_RACES){
            currentRun.incrementCurrentRaceIndexByOne()
        }
    }

    fun initializeOrContinueCurrentRun() {
        val run = runRepository.getCurrentRun()
        if (run == null) {
            startNewRun()
        } else {
            run.also { currentRun = it }
        }
    }



    fun isFinished(): Boolean {
        // Hack um Schneller auf Summuary zu kommen   return currentRun.currentRaceIndex>=5
         return currentRun.isCompleted()
    }
}
