package com.example.winnipegtransitoffline.api

import android.util.Log
import androidx.room.TypeConverter
import com.example.winnipegtransitoffline.api.model.Bus
import com.example.winnipegtransitoffline.api.model.Centre
import com.example.winnipegtransitoffline.api.model.Distances
import com.example.winnipegtransitoffline.api.model.Geographic
import com.example.winnipegtransitoffline.api.model.RouteSchedule
import com.example.winnipegtransitoffline.api.model.Stop
import com.example.winnipegtransitoffline.api.model.Times
import com.example.winnipegtransitoffline.api.model.Variant
import com.google.gson.Gson

class DataConverter {
    private val gson = Gson()
    @TypeConverter
    fun geographicFromString(serialized: String): Geographic {
        return gson.fromJson(serialized, Geographic::class.java)
    }
    @TypeConverter
    fun toString(entity: Geographic): String {
        return gson.toJson(entity)
    }
    @TypeConverter
    fun centreFromString(serialized: String): Centre {
        return gson.fromJson(serialized, Centre::class.java)
    }
    @TypeConverter
    fun toString(entity: Centre): String {
        return gson.toJson(entity)
    }
    @TypeConverter
    fun distancesFromString(serialized: String): Distances {
        return gson.fromJson(serialized, Distances::class.java)
    }
    @TypeConverter
    fun toString(entity: Distances): String {
        return gson.toJson(entity)
    }
    @TypeConverter
    fun busFromString(serialized: String): Bus {
        return gson.fromJson(serialized, Bus::class.java)
    }
    @TypeConverter
    fun toString(entity: Bus): String {
        return gson.toJson(entity)
    }
    @TypeConverter
    fun timeFromString(serialized: String): Times {
        return gson.fromJson(serialized, Times::class.java)
    }
    @TypeConverter
    fun toString(entity: Times): String {
        return gson.toJson(entity)
    }
    @TypeConverter
    fun variantFromString(serialized: String): Variant {
        return gson.fromJson(serialized, Variant::class.java)
    }
    @TypeConverter
    fun toString(entity: Variant): String {
        return gson.toJson(entity)
    }
    @TypeConverter
    fun stopFromString(serialized: String): Stop {
        return gson.fromJson(serialized, Stop::class.java)
    }
    @TypeConverter
    fun toString(entity: Stop): String {
        return gson.toJson(entity)
    }
    @TypeConverter
    fun routeScheduleFromString(serialized: String): List<RouteSchedule> {
        Log.i("Data Converter","$serialized")
        return gson.fromJson(serialized, List::class.java) as List<RouteSchedule>
    }
    @TypeConverter
    fun toString(entity: List<RouteSchedule>): String {
        return gson.toJson(entity)
    }

}