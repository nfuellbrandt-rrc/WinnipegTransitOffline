package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Distances(
    @Json(name = "direct")
    val direct: String?,
    @Json(name = "walking")
    val walking: String?
)