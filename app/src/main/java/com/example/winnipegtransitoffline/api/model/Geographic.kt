package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Geographic(
    @Json(name = "latitude")
    val latitude: String,
    @Json(name = "longitude")
    val longitude: String
)