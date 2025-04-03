package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.MarioKartApp
import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity
import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.data.database.entity.RunEntity_
import com.buffe.mariokarttimetracker.data.mapper.RaceMapper
import com.buffe.mariokarttimetracker.data.mapper.RunMapper
import com.buffe.mariokarttimetracker.ui.main.Run
import io.objectbox.Box
import io.objectbox.kotlin.query

class RunRepository {
    private val runBox: Box<RunEntity> = MarioKartApp.boxStore.boxFor(RunEntity::class.java)
    private val raceRepository=RaceRepository()


    fun updateRun(run: Run) {
        val existingEntity = runBox.get(run.id ?: 0)

        // Update nur die veränderlichen Felder
        existingEntity.startTime = run.startTime
        existingEntity.finished = run.isCompleted()
        existingEntity.currentRaceIndex = run.currentRaceIndex

        // Bestehende Races löschen und neu hinzufügen
        existingEntity.races.clear()
        existingEntity.races.addAll(run.races.map { RaceMapper.toEntity(it, existingEntity) })

        runBox.put(existingEntity)  // Wichtig: put() re-attached automatisch
    }

    fun deleteRun(run: Run) {
        runBox.remove(RunMapper.toEntity(run))
    }

    fun getRunById(runId: Long): Run {
        return runBox.get(runId).let { RunMapper.toDomain(it) }
    }

    fun getAllRuns(): List<Run> {
        return runBox.all.map { RunMapper.toDomain(it) }
    }

    fun getCurrentRun(): Run? {
        val query = runBox.query(RunEntity_.finished.equal(false)).build()

        val result = query.findFirst()
        query.close()
        return result?.let { RunMapper.toDomain(it) }
    }

    fun getTimeOfAverageCompleteRun(): Long {
        return getFinishedRuns().sumOf {
            sumOfRaceTimes(it.races)
        } / getNumberOfFinishedRuns()
    }

    fun getFinishedRuns(): List<RunEntity> {
         val query=runBox.query {
            equal(RunEntity_.finished, true)
            build()
        }
           val result=query.find()
        query.close()
        return result
    }

    private fun getNumberOfFinishedRuns(): Int {
        val query= runBox.query {
            equal(RunEntity_.finished, true)
        }
          val count=query.count().toInt()
          query.close()
        return count
    }

    fun getBestRunTimeOfAll(): Long {
        return getFinishedRuns().minOf { sumOfRaceTimes(it.races) }
    }

    fun sumOfRaceTimes(races: List<RaceEntity>): Long {
        return races.sumOf { it.raceTimeInMillis }
    }
    fun getBestRunTimeTillTrack(trackId:Int):Long{
       return getFinishedRuns().minOf { run-> sumOfRaceTimes(
            raceRepository.getAllRacesTillTrack(run.id,trackId))}
    }
    fun getAverageRunTimeTillTrack(trackId:Int):Long{
       return getFinishedRuns().sumOf { run-> sumOfRaceTimes(
            raceRepository.getAllRacesTillTrack(run.id,trackId))}/getNumberOfFinishedRuns()
    }

}


