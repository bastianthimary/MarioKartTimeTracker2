package com.buffe.mariokarttimetracker.ui.main

import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.data.repository.RaceRepository
import com.buffe.mariokarttimetracker.data.repository.RunRepository
import com.buffe.mariokarttimetracker.data.repository.TrackRepository
import com.buffe.mariokarttimetracker.util.TimeFormatUtils

class RunManager(trackRepository: TrackRepository) {
    private lateinit var currentRun: Run
    private val runRepository = RunRepository()
    private val raceRepository = RaceRepository()
    private val tracks: List<Track> = trackRepository.getAllTracks()
    fun startNewRun(): Run {
        currentRun = Run()
        return currentRun
    }

    fun getCurrentTrack(): Track {
        return tracks[currentRun.currentRaceIndex]
    }

    fun getCurrentRace(): Race? {
        return currentRun.races.getOrNull(currentRun.currentRaceIndex)
    }

    fun addCurrentRace(raceTime: RaceTime) {
        currentRun.addRace(Race(0, getCurrentTrack(), raceTime, null))
    }

    fun moveToNextRace() {
        currentRun.incrementCurrentRaceIndexByOne()
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

    fun getCurrentBestTimeFormatted(): String {
        return TimeFormatUtils.formatTime(raceRepository.getBestRaceTimeOfTrack(getCurrentTrack().id))
    }
    fun getCurrentAverageTimeFormatted():String{
        return TimeFormatUtils.formatTime(raceRepository.getAverageRaceTimeOfTrack(getCurrentTrack().id))
    }
}
