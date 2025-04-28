package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Variant(
    @Json(name = "key")
    val key: String,
    @Json(name = "name")
    val name: String
)