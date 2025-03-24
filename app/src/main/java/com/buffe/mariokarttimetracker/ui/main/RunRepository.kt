package com.buffe.mariokarttimetracker.ui.main

object RunRepository {
    val runs = mutableListOf<Run>()

    fun addRun(run: Run) {
        runs.add(run)
    }

    fun bestRun(): Run? {
        return runs.minByOrNull { it.totalTime() }
    }

    fun averageTime(): Long {
        if (runs.isEmpty()) return 0L
        return runs.sumOf { it.totalTime() } / runs.size
    }
}
