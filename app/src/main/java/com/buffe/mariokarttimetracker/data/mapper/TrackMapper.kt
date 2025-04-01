package com.buffe.mariokarttimetracker.data.mapper

import com.buffe.mariokarttimetracker.data.database.entity.TrackEntity
import com.buffe.mariokarttimetracker.data.model.Track


object TrackMapper {
    fun toDomain(entity: TrackEntity): Track {
        return Track(id = entity.id, name = entity.name)
    }

    fun toEntity(track: Track): TrackEntity {
        return TrackEntity(id = track.id, name = track.name)
    }
}
