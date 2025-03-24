package com.buffe.mariokarttimetracker.data


import com.buffe.mariokarttimetracker.ui.main.Run

class RunRepository {

    private val runs = mutableListOf<Run>()

    fun addRun(run: Run) {
        runs.add(run)
    }

    fun getAllRuns(): List<Run> {
        return runs.toList() // Kopie zurückgeben, um Mutable-Zugriff zu vermeiden
    }

    fun getRunById(id: Int): Run? {
        return runs.find { it.runId == id }
    }

    fun getLatestRun(): Run? {
        return runs.maxByOrNull { it.startTime } // Neuesten Run anhand der Startzeit holen
    }

    fun getFastestRun(): Run? {
        return runs.minByOrNull { it.calculateTotalTime().timeMillis}
    }
}
