package com.buffe.mariokarttimetracker.ui.main


import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.data.repository.RaceRepository
import com.buffe.mariokarttimetracker.data.repository.RunRepository
import com.buffe.mariokarttimetracker.util.TimeFormatUtils

class RunManager {
    private lateinit var currentRun: Run
    private val runRepository = RunRepository()
    private val raceRepository = RaceRepository()
    private val MAX_NUMBER_OF_RACES=96
    fun startNewRun(): Run {
        currentRun = Run()
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

    fun getCurrentRunTotalTimeFormatted(): String {
        return TimeFormatUtils.formatTime(currentRun.races.sumOf { it.raceTime.timeMillis })
    }

    fun getCurrentBestTotalTimeFormatted(): String {
        return TimeFormatUtils.formatTime(runRepository.getBestRunTimeTillTrack(getCurrentTrack().id))
    }

    fun getCurrentAverageTotalTimeFormatted(): String {
        return TimeFormatUtils.formatTime(runRepository.getAverageRunTimeTillTrack(getCurrentTrack().id))
    }
    fun getCurrentBestTrackTimeFormatted(): String {
        return TimeFormatUtils.formatTime(raceRepository.getBestRaceTimeOfTrack(getCurrentTrack().id))
    }

    fun getCurrentAverageTrackTimeFormatted(): String {
        return TimeFormatUtils.formatTime(raceRepository.getAverageRaceTimeOfTrack(getCurrentTrack().id))
    }

    fun isFinished(): Boolean {
        return currentRun.isCompleted()
    }
}
