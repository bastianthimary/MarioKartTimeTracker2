package com.buffe.mariokarttimetracker.ui.main

import com.buffe.mariokarttimetracker.util.TimeFormatUtils

data class RaceTime(val timeMillis: Long) {

    override fun toString(): String {
        return TimeFormatUtils.formatTime(timeMillis)
    }

    companion object {
        fun fromString(timeString: String): RaceTime? {
            val millis = TimeFormatUtils.parseTime(timeString) ?: return null
            return RaceTime(millis)
        }
    }
}

