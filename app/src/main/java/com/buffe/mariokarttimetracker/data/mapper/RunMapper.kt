package com.buffe.mariokarttimetracker.data.mapper

import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.ui.main.Run

object RunMapper {
    fun toDomain(runEntity: RunEntity): Run {
        return Run(
            id = runEntity.id,
            startTime = runEntity.startTime,
            currentTrack = TrackMapper.toDomain(runEntity.currentTrack.target),
            races = runEntity.races.map { entity -> RaceMapper.toDomain(entity) }.toMutableList()
        )
    }

    fun toEntity(run: Run): RunEntity {
        val runEntity = RunEntity(
            id = run.id ?: 0,
            startTime = run.startTime,
            finished = run.isCompleted(),

            )
        run.currentTrack?.let {
            runEntity.currentTrack.setAndPutTarget(TrackMapper.toEntity(run.currentTrack))
        }
        runEntity.races.addAll(run.races.map { RaceMapper.toEntity(it, run) })
        return runEntity
    }
}