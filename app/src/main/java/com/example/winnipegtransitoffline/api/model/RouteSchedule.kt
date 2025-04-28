package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteSchedule(
    @Json(name = "route")
    val route: Route,
    @Json(name = "scheduled-stops")
    val scheduledStops: List<ScheduledStop>
)