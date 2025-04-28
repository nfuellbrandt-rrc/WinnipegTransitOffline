package com.example.winnipegtransitoffline.api.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.winnipegtransitoffline.api.model.Stop
import com.example.winnipegtransitoffline.api.model.StopSchedule

@Dao
interface StopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(stops: StopSchedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStops(stops: List<Stop>)

    @Query("SELECT * FROM stops")
    suspend fun getAllStops(): List<Stop>

//    @Query("SELECT * FROM stops WHERE ")
}