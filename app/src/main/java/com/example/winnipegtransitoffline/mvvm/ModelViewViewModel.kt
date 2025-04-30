package com.example.winnipegtransitoffline.mvvm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.winnipegtransitoffline.api.StopsManager
import com.example.winnipegtransitoffline.api.db.AppDatabase
import com.example.winnipegtransitoffline.api.model.Stop
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class ModelViewViewModel(db: AppDatabase, stopsManager: StopsManager) {
    private var _stopsManager: StopsManager = stopsManager
    private var _db: AppDatabase = db

    var data by mutableStateOf<List<BusStopData>>(emptyList())
    var stop_data by mutableStateOf<Stop?>(null)

    fun setStopData(stopID: String) {
        val newData = mutableListOf<BusStopData>()
        this._stopsManager.getScheduleById(this._db ,stopID.toInt()) { stopData ->
            for (routeData in stopData.routeSchedules) {
                for (busData in routeData.scheduledStops) {
                    newData.add(
                        BusStopData(
                            busNumber = "${routeData.route.number}",
                            estimatedTimeOfArrival = busData.times.arrival?.estimated ?: "",
                            busRoute = busData.variant.name
                        )
                    )
                }
            }

            _stopsManager.getStopById(_db, stopID.toInt()) { stop ->
                this.stop_data = stop
            }
            Log.d("initial stop data", stop_data.toString())
            this.data = newData
                .filter { busStopData ->
                    try {
                        val etaFull = LocalDateTime.parse(busStopData.estimatedTimeOfArrival)
                        val now = LocalDateTime.now()
                        val today = now.dayOfWeek

                        if (etaFull.dayOfWeek != today) return@filter false

                        val etaToday = LocalDateTime.of(now.toLocalDate(), etaFull.toLocalTime())
                        etaToday.isAfter(now) && etaToday.isBefore(now.plusHours(2))
                    } catch (e: DateTimeParseException) {
                        false
                    }
                }
                .sortedBy { it.estimatedTimeOfArrival }
        }
    }
}
