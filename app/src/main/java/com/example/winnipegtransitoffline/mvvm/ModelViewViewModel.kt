package com.example.winnipegtransitoffline.mvvm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.winnipegtransitoffline.api.StopsManager
import com.example.winnipegtransitoffline.api.db.AppDatabase
import com.example.winnipegtransitoffline.api.model.Stop

class ModelViewViewModel(db: AppDatabase, stopsManager: StopsManager) {
    private var _stopsManager: StopsManager = stopsManager
    private var _db: AppDatabase = db

    val data = mutableListOf<BusStopData>()
    var stop_data by mutableStateOf<Stop?>(null)

    fun setStopData(stopID: String) {
        data.clear()
        this._stopsManager.getStopById(this._db ,stopID.toInt(), { stopData ->
            for (routeData in stopData.routeSchedules) {
                for(busData in routeData.scheduledStops) {
                    data.add(
                        BusStopData(
                            busNumber = "${routeData.route.number}",
                            estimatedTimeOfArrival = busData.times.arrival?.estimated ?: "",
                            busRoute = busData.variant.name
                        )
                    )
                }
            }

            this.stop_data = stopData.stop
            Log.i("abc", "$data")
            data.filter { it.estimatedTimeOfArrival.trim() != "" }
            data.sortBy { it.estimatedTimeOfArrival }
        })
    }
}
