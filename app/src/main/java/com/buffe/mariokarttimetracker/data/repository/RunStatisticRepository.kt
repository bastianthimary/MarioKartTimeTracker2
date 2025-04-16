package com.buffe.mariokarttimetracker.data.repository

import com.buffe.mariokarttimetracker.MarioKartApp
import com.buffe.mariokarttimetracker.data.database.entity.RunStatisticEntity
import com.buffe.mariokarttimetracker.data.mapper.RunStatisticsMapper
import com.buffe.mariokarttimetracker.data.model.RunStatistic
import io.objectbox.Box

class RunStatisticRepository {
    private val runStatisticBox: Box<RunStatisticEntity> = MarioKartApp.boxStore.boxFor(RunStatisticEntity::class.java)

    fun getOrCreateRunStatistic():RunStatistic{
       val savedStatistic=  runStatisticBox.get(1)
        if(savedStatistic==null){
            val entity= RunStatisticEntity()
            runStatisticBox.put(entity)
            return RunStatisticsMapper.toDomain(entity)
        }
        return RunStatisticsMapper.toDomain(savedStatistic)
    }

    fun saveStatistic(saveEntity: RunStatistic){
        runStatisticBox.put(RunStatisticsMapper.toEntity(saveEntity))
    }
    fun deleteStatistic(){
        runStatisticBox.remove(1)
    }

}