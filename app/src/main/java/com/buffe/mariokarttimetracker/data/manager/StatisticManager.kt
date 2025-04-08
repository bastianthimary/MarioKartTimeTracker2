package com.buffe.mariokarttimetracker.data.manager

import com.buffe.mariokarttimetracker.data.model.Run
import com.buffe.mariokarttimetracker.data.repository.RaceRepository
import com.buffe.mariokarttimetracker.data.repository.RunRepository
import com.buffe.mariokarttimetracker.util.TimeFormatUtils
import io.objectbox.annotation.Id

class StatisticManager {
    private val runRepository = RunRepository()
    private val raceRepository = RaceRepository()
    private val FORMATED_TIME_ZERO="0:00.000"
    fun getCurrentRunTotalTimeFormatted(currentRun: Run): String {
        return TimeFormatUtils.calcRunsTotalTime(currentRun)
    }


    fun getCurrentBestTotalTimeFormatted(trackId: Int): String {
        if(trackId==1){
            return FORMATED_TIME_ZERO
        }
        return TimeFormatUtils.formatTime(runRepository.getBestRunTimeTillTrack(trackId-1))
    }

    fun getAverageTotalTimeFormattedBeforeCurrent(trackId: Int): String {
        if(trackId==1){
            return FORMATED_TIME_ZERO
        }
        return TimeFormatUtils.formatTime(runRepository.getAverageRunTimeTillTrack(trackId-1))
    }
    fun getCurrentBestTrackTimeFormatted(trackId: Int): String {
        return TimeFormatUtils.formatTime(raceRepository.getBestRaceTimeOfTrack(trackId))
    }

    fun getCurrentAverageTrackTimeFormatted(trackId: Int): String {
        return TimeFormatUtils.formatTime(raceRepository.getAverageRaceTimeOfTrack(trackId))
    }

    fun determineRank(list: List<Long>, target: Long): Int {
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