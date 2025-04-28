package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Departure(
    @Json(name = "estimated")
    val estimated: String,
    @Json(name = "scheduled")
    val scheduled: String
)