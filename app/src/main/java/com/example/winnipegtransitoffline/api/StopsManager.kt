package com.example.winnipegtransitoffline.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.winnipegtransitoffline.BuildConfig
import com.example.winnipegtransitoffline.api.db.AppDatabase
import com.example.winnipegtransitoffline.api.model.ScheduleData
import com.example.winnipegtransitoffline.api.model.Stop
import com.example.winnipegtransitoffline.api.model.StopData
import com.example.winnipegtransitoffline.api.model.StopSchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class StopsManager(database: AppDatabase) {
    private var _stopsResponse = mutableStateOf<List<Stop>>(emptyList())
    private var stop = mutableStateOf<StopSchedule?>(null)

    private val db = database


    val api_key = BuildConfig.TRANSIT_API_KEY
    val stopsResponse: MutableState<List<Stop>>
        @Composable get() = remember {
            _stopsResponse
        }

    init {
        getStopsFromRoom(db)
        getStops(db)
        Log.i("test", "This got run")
    }

    private fun getStopsFromRoom(database: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            _stopsResponse.value = database.stopDao().getAllStops()
        }
    }

    private fun getStops(database: AppDatabase) {
        val service = Api.retrofitService.getNearbyStops(api_key, "200", "49.895", "-97.138")

        service.enqueue(object : retrofit2.Callback<StopData>{
            override fun onResponse(
                call: Call<StopData>,
                response: Response<StopData>
            ) {
                if (response.isSuccessful) {
                    Log.i("asd", response.raw().toString())
                    Log.i("Data", "Data is loaded")

                    _stopsResponse.value += (response.body()?.stops ?: emptyList())
//                    Log.i("DataStream", _stopsResponse.value.toString())

                    CoroutineScope(Dispatchers.IO).launch {
                        saveDataListToDatabase(database = database, _stopsResponse.value)
                    }
                }
            }

            override fun onFailure(
                call: Call<StopData>,
                t: Throwable
            ) {
                Log.d("error", "${t.message}")
            }
        })
    }

    // need to create 3 requests saturday, sunday, weekday
    // StopSchedule.date
    private fun getSchedulesById(database: AppDatabase, id: Int, onComplete: (StopSchedule) -> Unit) {
        val dates = arrayOf("Weekday", "Saturday", "Sunday")
        var times = arrayOf<Calendar>()

        var currentDate: Calendar = Calendar.getInstance()
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.AM_PM, Calendar.AM)

        val weekday = currentDate.get(Calendar.DAY_OF_WEEK)

        currentDate.add(Calendar.DATE, 9 - weekday)
        times += currentDate.clone() as Calendar
        currentDate.add(Calendar.DATE, 5)
        times += currentDate.clone() as Calendar
        currentDate.add(Calendar.DATE, 1)
        times += currentDate.clone() as Calendar
        for (i in 0..2) {

            val endTime = times[i].clone() as Calendar
            val startTime = times[i].clone() as Calendar
            // 24 hour range
            endTime.add(Calendar.DATE, 1)

            val service = Api.retrofitService.getStopById(
                apiKey = api_key,
                id = id,
                startTime = calendarToTimestamp(startTime),
                endTime = calendarToTimestamp(endTime)
            )

            service.enqueue(object : retrofit2.Callback<ScheduleData>{
                override fun onResponse(
                    call: Call<ScheduleData>,
                    response: Response<ScheduleData>
                ) {
                    Log.i("network request", "suffering but over here")
                    if (response.isSuccessful) {

                        Log.e("request", "${response.body()}")
                        stop.value = (response.body()?.stopSchedule)

                        stop.value?.date = dates[i]

                        Log.i("DataStream", "${stop.value}")

                        CoroutineScope(Dispatchers.IO).launch {
                            saveScheduleDataToDatabase(database = database, stop.value!!)
                            onComplete(stop.value!!)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ScheduleData>,
                    t: Throwable
                ) {
                    Log.d("pain&suffering", call.request().toString())
                    Log.d("error:getStopsById", "${t.message}")
                }
            })
        }
    }

    public fun getScheduleById(db: AppDatabase, id: Int, onComplete: (StopSchedule) -> Unit): StopSchedule? {
        getSchedulesById(database = db, id = id, onComplete)
        return stop.value
    }


    private fun saveDataListToDatabase(database: AppDatabase, stopData: List<Stop>) {
        CoroutineScope(Dispatchers.IO).launch {
            database.stopDao().insertAllStops(stopData)
        }
    }
    private fun saveScheduleDataToDatabase(database: AppDatabase, scheduleData: StopSchedule) {
        CoroutineScope(Dispatchers.IO).launch {
            database.stopDao().insertSchedule(scheduleData)
        }
    }

    fun setFavoriteStop(database: AppDatabase, stopId: Int, isFavorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val stop = database.stopDao().getStopByID(stopId)
            Log.i("Stop", stop.toString())
            stop.isFavorite = isFavorite
            Log.i("Stop", stop.toString())
            database.stopDao().updateStopFavorite(stop)
            _stopsResponse.value = database.stopDao().getAllStops()
        }
    }

    fun getStopById(database: AppDatabase, number: Int, callback: (Stop) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val stop = database.stopDao().getStopByID(number)
            callback(stop)
        }
    }

    private fun calendarToTimestamp(time: Calendar): String {
        return String.format(
            "%s-%s-%sT%s:%s:%s",
            time.get(Calendar.YEAR),
            (time.get(Calendar.MONTH) + 1).toString().padStart(2, '0'),
            time.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0'),
            time.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0'),
            time.get(Calendar.MINUTE).toString().padStart(2, '0'),
            time.get(Calendar.SECOND).toString().padStart(2, '0')
        )
    }
}



// stops/10185/schedule?start=2025-04-22T23:00:00&end=2025-04-23T02:00:00