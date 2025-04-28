package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleData(
    @Json(name = "query-time")
    val queryTime: String,
    @Json(name = "stop-schedule")
    val stopSchedule: StopSchedule?
)