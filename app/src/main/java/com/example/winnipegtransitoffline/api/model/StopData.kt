package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StopData(
    @Json(name = "query-time")
    val queryTime: String,
    @Json(name = "stops")
    val stops: List<Stop>
)