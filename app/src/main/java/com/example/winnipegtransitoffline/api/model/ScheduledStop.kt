package com.example.winnipegtransitoffline.api.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "stop-schedules")
@JsonClass(generateAdapter = true)
data class ScheduledStop(
//    @Json(name = "bus")
//    val bus: Bus,
    @Json(name = "cancelled")
    val cancelled: String,
    @PrimaryKey(autoGenerate = false)
    @Json(name = "key")
    val key: String,
    @Json(name = "times")
    val times: Times,
    @Json(name = "variant")
    val variant: Variant
)