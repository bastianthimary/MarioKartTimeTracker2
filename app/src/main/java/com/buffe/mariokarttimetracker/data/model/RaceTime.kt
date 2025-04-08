package com.buffe.mariokarttimetracker.data.model

import com.buffe.mariokarttimetracker.util.TimeFormatUtils
import java.io.Serializable

data class RaceTime(val timeMillis: Long):Serializable {
    constructor(timeString: String):this(TimeFormatUtils.parseTime(timeString))

    override fun toString(): String {
        return TimeFormatUtils.formatTime(timeMillis)
    }

    companion object {
        fun fromString(timeString: String): RaceTime {
            val millis = TimeFormatUtils.parseTime(timeString)
            return RaceTime(millis)
        }
    }
}

