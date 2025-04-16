package com.buffe.mariokarttimetracker.data.mapper




import com.buffe.mariokarttimetracker.data.database.entity.RunStatisticEntity
import com.buffe.mariokarttimetracker.data.model.RunStatistic
import com.google.gson.Gson

object RunStatisticsMapper {

    private val gson = Gson()

    fun toEntity(stat: RunStatistic): RunStatisticEntity {
        return RunStatisticEntity(
            recordCount = stat.recordCount,
            placementsJson = gson.toJson(stat.trackPlacements)
        )
    }

    fun toDomain(entity: RunStatisticEntity): RunStatistic {
        val placements: MutableMap<Int, Int> = gson.fromJson(entity.placementsJson, MutableMap::class.java)
            ?.map { (key, value) -> key.toString().toInt() to (value as Number).toInt() }
            ?.toMap()
            ?.toMutableMap() ?: mutableMapOf()
        return RunStatistic(
            recordCount = entity.recordCount,
            trackPlacements = placements
        )
    }
}
