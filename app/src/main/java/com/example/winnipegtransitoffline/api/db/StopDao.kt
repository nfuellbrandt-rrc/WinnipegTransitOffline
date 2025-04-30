package com.example.winnipegtransitoffline.api.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.winnipegtransitoffline.api.model.Stop
import com.example.winnipegtransitoffline.api.model.StopSchedule

@Dao
interface StopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedule(stops: StopSchedule)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllStops(stops: List<Stop>)

    @Query("SELECT * FROM stops")
    suspend fun getAllStops(): List<Stop>

    @Query("SELECT * FROM stops WHERE number == :key")
    suspend fun getStopByID(key: Int): Stop

    @Update
    suspend fun updateStopFavorite(stop: Stop)

    @Query("DELETE FROM StopSchedules WHERE stop LIKE '%number%' || :number || '%'")
    suspend fun deleteOldSchedule(number: String)
//    @Query("SELECT * FROM stops WHERE ")
}