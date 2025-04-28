package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BadgeStyle(
    @Json(name = "background-color")
    val backgroundColor: String,
    @Json(name = "border-color")
    val borderColor: String,
    @Json(name = "class-names")
    val classNames: ClassNames,
    @Json(name = "color")
    val color: String
)