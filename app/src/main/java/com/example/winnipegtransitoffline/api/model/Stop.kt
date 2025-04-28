package com.example.winnipegtransitoffline.api.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "stops")
@JsonClass(generateAdapter = true)
data class Stop(
    @Json(name = "centre")
    val centre: Centre,
    @Json(name = "direction")
    val direction: String,
    @Json(name = "distances")
    val distances: Distances?,
    @PrimaryKey(autoGenerate = false)
    @Json(name = "key")
    val key: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "number")
    val number: Int,
    @Json(name = "side")
    val side: String
)