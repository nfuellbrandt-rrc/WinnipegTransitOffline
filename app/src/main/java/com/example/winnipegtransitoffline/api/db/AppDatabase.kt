package com.example.winnipegtransitoffline.api.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.winnipegtransitoffline.api.DataConverter
import com.example.winnipegtransitoffline.api.model.Stop
import com.example.winnipegtransitoffline.api.model.StopSchedule


@Database(entities = [Stop::class, StopSchedule::class], version = 6, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stopDao() : StopDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "WinnipegTransitOffline"
                ).fallbackToDestructiveMigration().build()
                val dbFile = context.getDatabasePath("WinnipegTransitOffline")
                Log.d("DB_CHECK", "Exists? ${dbFile.exists()}, Path: ${dbFile.absolutePath}")
                INSTANCE = instance
                return instance
            }
        }
    }
}