package com.example.winnipegtransitoffline.api.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "StopSchedules")
@JsonClass(generateAdapter = true)
data class StopSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Json(name = "route-schedules")
    val routeSchedules: List<RouteSchedule>,
    @Json(name = "stop")
    val stop: Stop,
    var date: String = "Sunday"
)