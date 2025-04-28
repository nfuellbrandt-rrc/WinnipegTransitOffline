package com.example.winnipegtransitoffline.api

import com.example.winnipegtransitoffline.api.model.ScheduleData
import com.example.winnipegtransitoffline.api.model.StopData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StopsService {
    @GET("stops.json")
    fun getNearbyStops(
        @Query("api-key") apiKey: String,
        @Query("distance") distance: String,
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Call<StopData>

    @GET("stops/{id}/schedule.json")
    fun getStopById(
        @Path("id") id: Int,
        @Query("api-key") apiKey: String,
        @Query("start") startTime: String,
        @Query("end") endTime: String
    ) : Call<ScheduleData>
}