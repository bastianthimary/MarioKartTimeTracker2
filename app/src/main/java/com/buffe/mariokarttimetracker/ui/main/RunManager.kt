package com.buffe.mariokarttimetracker.ui.main

import com.buffe.mariokarttimetracker.data.model.Race
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.data.repository.TrackRepository

class RunManager(private val trackRepository: TrackRepository) {
    private var currentRun: Run? = null

    private val tracks: List<Track> = trackRepository.getAllTracks()
    fun startNewRun(): Run {
        currentRun = Run()
        return currentRun!!
    }

    fun getCurrentTrack():Track{
       return tracks[currentRun?.currentRaceIndex!!]
    }
    fun getCurrentRace(): Race? {
        return currentRun?.races?.getOrNull(currentRun!!.currentRaceIndex)
    }

    fun setRaceTime(raceTime: RaceTime) {
        currentRun?.addRace(Race(0,getCurrentTrack(),raceTime,null))
    }

    fun moveToNextRace(): Boolean {
        return if (currentRun != null) {
            currentRun!!.currentRaceIndex
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
