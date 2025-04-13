package com.buffe.mariokarttimetracker.data.manager

import com.buffe.mariokarttimetracker.data.model.RaceTime
import com.buffe.mariokarttimetracker.data.model.Run
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.data.repository.RaceRepository
import com.buffe.mariokarttimetracker.data.repository.RunRepository
import com.buffe.mariokarttimetracker.util.TimeFormatUtils

class StatisticManager {
    private val runRepository = RunRepository()
    private val raceRepository = RaceRepository()
    private val FORMATED_TIME_ZERO = "0:00.000"
    var currentTotalTime = RaceTime(FORMATED_TIME_ZERO)
    var bestTotalTime = RaceTime(FORMATED_TIME_ZERO)
    var averageTotalTime = RaceTime(FORMATED_TIME_ZERO)


    fun getCurrentRunTotalTimeFormatted(currentRun: Run): String {
        currentTotalTime = RaceTime(TimeFormatUtils.calcRunsTotalTime(currentRun))
        return TimeFormatUtils.hourFormatTime(currentTotalTime.timeMillis)
    }


    fun determineCurrentBestTotalTimeFormatted(trackId: Int): String {
        if (trackId == 1) {
            return FORMATED_TIME_ZERO
        }
        bestTotalTime =
            RaceTime(TimeFormatUtils.formatTime(runRepository.getBestRunTimeTillTrack(trackId - 1)))
        return TimeFormatUtils.hourFormatTime(bestTotalTime.timeMillis)
    }

    fun determineAverageTotalTimeFormattedBeforeCurrent(trackId: Int): String {
        if (trackId == 1) {
            return FORMATED_TIME_ZERO
        }
        averageTotalTime =
            RaceTime(TimeFormatUtils.formatTime(runRepository.getAverageRunTimeTillTrack(trackId - 1)))
        return averageTotalTime.toString()
    }

    fun determineCurrentBestTrackTimeFormatted(trackId: Int): String {
        return TimeFormatUtils.formatTime(raceRepository.getBestRaceTimeOfTrack(trackId))
    }

    fun determineCurrentAverageTrackTimeFormatted(trackId: Int): String {
        return TimeFormatUtils.formatTime(raceRepository.getAverageRaceTimeOfTrack(trackId))
    }

    fun determineRankOfCurrentRun(currentRun: Run, currentTrack: Track): Int {
        if (currentTrack.id == 1) {
            return 0
        }
        val allRunTimesTillTrack = runRepository.getAllRuns()
            .map {
                runRepository.sumOfRaceTimes(
                    raceRepository.getAllRacesTillTrack(
                        currentRun.id!!,
                        currentTrack.id
                    )
                )
            }
        val currentRunTime = currentRun.races.sumOf { it.raceTime.timeMillis }
        return determineRank(allRunTimesTillTrack, currentRunTime)
    }

    private fun determineRank(list: List<Long>, target: Long): Int {
        val sortedList = list.sorted()
        val index = sortedList.binarySearch(target)
        return if (index >= 0) {
            var firstIndex = index
            while (firstIndex > 0 && sortedList[firstIndex - 1] == target) {
                firstIndex--
            }
            firstIndex + 1
        } else {
            val insertionPoint = -(index + 1)
            insertionPoint + 1
        }
    }
}