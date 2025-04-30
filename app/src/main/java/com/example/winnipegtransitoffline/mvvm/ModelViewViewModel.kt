package com.example.winnipegtransitoffline.mvvm

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winnipegtransitoffline.api.StopsManager
import com.example.winnipegtransitoffline.api.db.AppDatabase
import com.example.winnipegtransitoffline.api.model.Stop
import com.example.winnipegtransitoffline.api.model.StopSchedule
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * A viewModel to abstract logic outside of composables.
 * @param db The database used to store data.
 * @param stopsManager The stopsManager used to request data.
 * @param context The context used to determine if the user is online.
 */
class ModelViewViewModel(
    private val db: AppDatabase,
    private val stopsManager: StopsManager,
    private val context: Context
) : ViewModel() {

    var data by mutableStateOf<List<BusStopData>>(emptyList())
    var stop_data by mutableStateOf<Stop?>(null)

    /**
     * Sets [data] and [stop_data] to correct data based on the stopID.
     * @param stopID The stop to get data about.
     */
    fun setStopData(stopID: String) {
        viewModelScope.launch {
            val stopData = fetchScheduleData(stopID)
            getStopData(stopID)
            data = stopData.routeSchedules.let { routeSchedules ->
                Log.i("routeData", stopData.stop.toString())
                routeSchedules
                    .flatMap { route ->
                        route.scheduledStops.map { busData ->
                            BusStopData(
                                busNumber = "${route.route.number}",
                                estimatedTimeOfArrival = busData.times.arrival?.estimated ?: "",
                                busRoute = busData.variant.name
                            )
                        }
                    }
                    .filter { busStopData ->
                        try {
                            val etaFull = LocalDateTime.parse(busStopData.estimatedTimeOfArrival)
                            val now = LocalDateTime.now()

                            if (etaFull.dayOfWeek != now.dayOfWeek && !(etaFull.dayOfWeek.value in 1..5 && now.dayOfWeek.value in 1..5)) {
                                    return@filter false
                            }
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


    /**
     * Used to get schedule data from the stopsManager.
     * @param stopID The stop to get schedule data about.
     */
    private suspend fun fetchScheduleData(stopID: String): StopSchedule {
        return suspendCoroutine { continuation ->
            stopsManager.getScheduleById(db, stopID.toInt(), context = context) { stopData ->
                continuation.resume(stopData)
            }
        }
    }

    /**
     * Used to get stop data from the stopsManager.
     * @param stopID The stop to get data about.
     */
    private fun getStopData(stopID: String) {
        stopsManager.getStopById(db, stopID.toInt()) { stop ->
            this.stop_data = stop
        }
    }
}
