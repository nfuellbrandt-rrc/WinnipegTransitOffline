package com.example.winnipegtransitoffline.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Route(
    @Json(name = "badge-label")
    val badgeLabel: Int,
    @Json(name = "badge-style")
    val badgeStyle: BadgeStyle,
    @Json(name = "coverage")
    val coverage: String,
    @Json(name = "customer-type")
    val customerType: String,
    @Json(name = "key")
    val key: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "number")
    val number: Int
)