package com.buffe.mariokarttimetracker.data.mapper

import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.ui.main.Run

object RunMapper {
    fun toDomain(runEntity: RunEntity): Run {
        return Run(
            id = runEntity.id,
            startTime = runEntity.startTime,
            races = runEntity.races.map { RaceMapper.toDomain(it) }.toMutableList(),
            currentRaceIndex = runEntity.currentRaceIndex
        )
    }

    fun toEntity(run: Run): RunEntity {
        val runEntity = RunEntity(
            id = run.id ?: 0,
            startTime = run.startTime,
            finished = run.isCompleted(),
            currentRaceIndex = run.currentRaceIndex
        )
        val raceEntities = run.races.map { RaceMapper.toEntity(it, runEntity) }
        runEntity.races.addAll(raceEntities)
        return runEntity
    }
}