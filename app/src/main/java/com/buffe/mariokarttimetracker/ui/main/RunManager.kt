package com.buffe.mariokarttimetracker.ui.main

import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.data.repository.TrackRepository

class RunManager(private val trackRepository: TrackRepository) {
    private var currentRun: Run? = null
    private var currentRaceIndex = 0
    private val tracks: List<Track> = trackRepository.getAllTracks()
    fun startNewRun(): Run {
        currentRun = Run()
        currentRaceIndex = 0
        return currentRun!!
    }

    fun getCurrentTrack():Track{
       return tracks[currentRaceIndex]
    }
    fun getCurrentRace(): Race? {
        return currentRun?.races?.getOrNull(currentRaceIndex)
    }

    fun setRaceTime(raceTime: RaceTime) {
        currentRun?.addRace(Race(getCurrentTrack(),raceTime,null))
    }

    fun moveToNextRace(): Boolean {
        return if (currentRun != null && currentRaceIndex == currentRun!!.races.size-1) {
            currentRaceIndex=currentRaceIndex++
            true
        } else {
            false
        }
    }

    fun getTotalRunTime(): RaceTime {
        val totalMillis = currentRun?.races?.sumOf { it.raceTime?.timeMillis ?: 0 } ?: 0
        return RaceTime(totalMillis)
    }
}
