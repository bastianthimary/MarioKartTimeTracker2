package com.buffe.mariokarttimetracker.data.mapper

import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.data.model.Run

object RunMapper {
    fun toDomain(runEntity: RunEntity): Run {
        return Run(
            id = runEntity.id,
            startTime = runEntity.startTime,
            races = runEntity.races.map { RaceMapper.toDomain(it) }.toMutableList(),
            currentRaceIndex = runEntity.currentRaceIndex
        )
    }

    fun toEntity(run: Run, existingEntity: RunEntity? = null): RunEntity {
        val runEntity = existingEntity ?: RunEntity(
            id = run.id ?: 0,
            startTime = run.startTime,
            finished = run.isCompleted(),
            currentRaceIndex = run.currentRaceIndex
        )

        // Nur bei neuer Entity Felder setzen
        if (existingEntity == null) {
            runEntity.id = run.id ?: 0
            runEntity.startTime = run.startTime
            runEntity.finished = run.isCompleted()
            runEntity.currentRaceIndex = run.currentRaceIndex
        }

        // Races immer neu mappen
        val raceEntities = run.races.map { RaceMapper.toEntity(it, runEntity) }
        runEntity.races.clear()
        runEntity.races.addAll(raceEntities)

        return runEntity
    }
}