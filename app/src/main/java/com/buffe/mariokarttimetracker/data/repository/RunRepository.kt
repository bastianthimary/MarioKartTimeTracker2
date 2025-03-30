package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.MarioKartApp
import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.data.database.entity.RunEntity_
import com.buffe.mariokarttimetracker.data.mapper.RunMapper
import com.buffe.mariokarttimetracker.ui.main.Run
import io.objectbox.Box
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.query

class RunRepository {
    private val runBox: Box<RunEntity> = MarioKartApp.boxStore.boxFor(RunEntity::class.java)
     fun insertRun(run: Run) {
        runBox.put(RunMapper.toEntity(run))
    }

     fun updateRun(run: Run) {
        runBox.put(RunMapper.toEntity(run))
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
    fun getCurrentRun():Run?{
       return runBox.query {
           equal(RunEntity_.finished,false)
           build()
       }.findFirst()?.let { RunMapper.toDomain(it) }
    }
}


