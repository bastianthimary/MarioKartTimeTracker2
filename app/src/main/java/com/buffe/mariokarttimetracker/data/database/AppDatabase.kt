package com.buffe.mariokarttimetracker.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.buffe.mariokarttimetracker.data.database.dao.TrackDao
import com.buffe.mariokarttimetracker.data.database.entity.RaceEntity
import com.buffe.mariokarttimetracker.data.database.entity.RunEntity
import com.buffe.mariokarttimetracker.data.database.entity.TrackEntity

@Database(entities = [TrackEntity::class,RunEntity::class, RaceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mario_kart_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}