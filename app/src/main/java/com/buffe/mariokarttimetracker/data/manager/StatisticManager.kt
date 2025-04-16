package com.buffe.mariokarttimetracker.data.manager

import com.buffe.mariokarttimetracker.data.model.RaceTime
import com.buffe.mariokarttimetracker.data.model.Run
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.data.repository.RaceRepository
import com.buffe.mariokarttimetracker.data.repository.RunRepository
import com.buffe.mariokarttimetracker.data.repository.RunStatisticRepository
import com.buffe.mariokarttimetracker.util.TimeFormatUtils

class StatisticManager {
    private val runRepository = RunRepository()
    private val raceRepository = RaceRepository()
    private val runStatisticRepository = RunStatisticRepository()
    val runStatistic = runStatisticRepository.getOrCreateRunStatistic()
    private val FORMATED_TIME_ZERO = "0:00.000"
    var currentTotalTime = RaceTime(FORMATED_TIME_ZERO)
    var bestTotalTime = RaceTime(FORMATED_TIME_ZERO)
    var averageTotalTime = RaceTime(FORMATED_TIME_ZERO)

    fun saveStatistic(currentRun: Run) {
        var currentRaceIndex = currentRun.currentRaceIndex
        currentRaceIndex--
        val currentRecord = raceRepository.getBestRaceTimeOfTrack(currentRaceIndex)
        val currentRacetimeMillis = currentRun.races[currentRaceIndex - 1].raceTime.timeMillis
        if (isCurrentFastestTime(currentRacetimeMillis, currentRecord)) {
            runStatistic.addRecord()
        }
        runStatistic.updateTrackPlacement(
            currentRaceIndex,
            determineRank(getAllRunTimesOfTrack(currentRaceIndex), currentRacetimeMillis)
        )
        runStatisticRepository.saveStatistic(runStatistic)
    }

    fun isCurrentFastestTime(currentTime: Long, otherTime: Long): Boolean {
        return currentTime == otherTime
    }

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
    fun determineTimeDifferenceToBest():Long{
        return bestTotalTime.timeMillis.minus(currentTotalTime.timeMillis)
    }
    fun determineAverageTotalTimeFormattedBeforeCurrent(trackId: Int): String {
        if (trackId == 1) {
            return FORMATED_TIME_ZERO
        }
        averageTotalTime =
            RaceTime(TimeFormatUtils.formatTime(runRepository.getAverageRunTimeTillTrack(trackId - 1)))
        return TimeFormatUtils.hourFormatTime(averageTotalTime.timeMillis)
    }
    fun determineAverageTotalTime():Long{
        return runRepository.getAverageRunTimeTillTrack(Track.entries.size)
    }
    fun determineBestTotalTime():Long{
        return runRepository.getBestRunTimeOfAll()
    }
    fun determineTimeDifferenceToAverage():Long{
        return averageTotalTime.timeMillis.minus(currentTotalTime.timeMillis)
    }
    fun determineCurrentBestTrackTimeFormatted(trackId: Int): String {
        return TimeFormatUtils.formatTime(raceRepository.getBestRaceTimeOfTrack(trackId))
    }

    fun determineCurrentAverageTrackTimeFormatted(trackId: Int): String {
        return TimeFormatUtils.formatTime(raceRepository.getAverageRaceTimeOfTrack(trackId))
    }

    fun determineRankOfCurrentRun(currentRun: Run): Int {
        if (currentRun.currentRaceIndex == 1) {
            return 0
        }

        val allRunTimesTillTrack = getAllRunTimesTillTrack(currentRun)
        val currentRunTime = currentRun.races.sumOf { it.raceTime.timeMillis }
        return determineRank(allRunTimesTillTrack, currentRunTime)
    }

    private fun getAllRunTimesTillTrack(
        currentRun: Run,
    ) = runRepository.getAllRuns()
        .map {
            runRepository.sumOfRaceTimes(
                raceRepository.getAllRacesTillTrack(
                    it.id!!,
                    currentRun.currentRaceIndex
                )
            )
        }

    private fun getAllRunTimesOfTrack(
        trackId: Int,
    ) = raceRepository.getAllRacesByTrack(
            trackId
        ).map { it.raceTimeInMillis }



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

fun clearStatistic() {
    runStatisticRepository.deleteStatistic()
}

fun getNumberOfRuns(): Int {
    return runRepository.getAllRuns().size
}
}