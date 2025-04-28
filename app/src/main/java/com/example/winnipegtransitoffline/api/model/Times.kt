package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Times(
    @Json(name = "arrival")
    val arrival: Arrival? = null,
    @Json(name = "departure")
    val departure: Departure
)