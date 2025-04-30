package com.example.winnipegtransitoffline.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

/**
 * Manager used to interact with the Room database and the Winnipeg Transit API.
 * @param database The database used to store data.
 */
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
    }

    /**
     * Gets all the stops in the database.
     * @param database The database with the data.
     */
    private fun getStopsFromRoom(database: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            _stopsResponse.value = database.stopDao().getAllStops()
        }
    }

    /**
     * Gets list of stops from the database.
     * @param database The database used to store the data.
     */
    private fun getStops(database: AppDatabase) {
        val service = Api.retrofitService.getNearbyStops(api_key, "2000", "49.895", "-97.138")

        service.enqueue(object : retrofit2.Callback<StopData>{
            override fun onResponse(
                call: Call<StopData>,
                response: Response<StopData>
            ) {
                Log.d("resault", response.toString())
                if (response.isSuccessful) {

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

    /**
     * Gets schedule data from the API.
     * @param database The database used to store the information.
     * @param id The stop id to get schedules for.
     * @param context The context used to determine if the user is online.
     * @param onComplete Lambda run after data is gotten.
     */
    private fun getSchedulesById(database: AppDatabase, id: Int, context: Context, onComplete: (StopSchedule) -> Unit) {
        if (isOnline(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                Log.i("Deleted stop", id.toString())
                database.stopDao().deleteOldSchedule(id.toString())
            }
        }

        val dates = arrayOf("Weekday", "Saturday", "Sunday")
        var times = arrayOf<Calendar>()

        val currentDate: Calendar = Calendar.getInstance()
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

        val deferreds = mutableListOf<CompletableDeferred<Unit>>()

        for (i in 0..2) {
            val deferred = CompletableDeferred<Unit>()
            deferreds.add(deferred)

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

            service.enqueue(object : retrofit2.Callback<ScheduleData> {
                override fun onResponse(
                    call: Call<ScheduleData>,
                    response: Response<ScheduleData>
                ) {
                    if (response.isSuccessful) {
                        stop.value = response.body()?.stopSchedule

                        stop.value?.date = dates[i]

                        CoroutineScope(Dispatchers.IO).launch {
                            saveScheduleDataToDatabase(database = database, stop.value!!)
                        }
                        deferred.complete(Unit)
                    }
                }

                override fun onFailure(
                    call: Call<ScheduleData>,
                    t: Throwable
                ) {
                    Log.d("pain&suffering", call.request().toString())
                    Log.d("error:getStopsById", "${t.message}")
                    deferred.complete(Unit)
                }
            })
        }

        CoroutineScope(Dispatchers.Main).launch {
            deferreds.awaitAll()

            onComplete(stop.value!!)
        }
    }

    /**
     * Gets a schedule by it's id.
     * @param db The database to get data from.
     * @param id The id of the stop schedule.
     * @param context Context used to check if user is online.
     * @param onComplete Lambda run when all data is saved.
     * @return The stop schedule for the stop.
     */
    fun getScheduleById(db: AppDatabase, id: Int, context: Context, onComplete: (StopSchedule) -> Unit): StopSchedule? {
        getSchedulesById(database = db, id = id, context = context, onComplete)

        return stop.value
    }

    /**
     * Saves a list of stop data to the database.
     * @param database The database to save data to.
     * @param stopData The list of stops to save.
     */
    private fun saveDataListToDatabase(database: AppDatabase, stopData: List<Stop>) {
        CoroutineScope(Dispatchers.IO).launch {
            database.stopDao().insertAllStops(stopData)
        }
    }

    /**
     * Saves Schedule data to the database.
     * @param database The database to save data to.
     * @param scheduleData The Schedule to save.
     */
    private fun saveScheduleDataToDatabase(database: AppDatabase, scheduleData: StopSchedule) {
        CoroutineScope(Dispatchers.IO).launch {
            database.stopDao().insertSchedule(scheduleData)
        }
    }

    /**
     * Marks a stop as a favorite in the database.
     * @param database The database storing the data.
     * @param stopId The id of the stop.
     * @param isFavorite Whether to mark it as favorite or take away favourite mark.
     */
    fun setFavoriteStop(database: AppDatabase, stopId: Int, isFavorite: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val stop = database.stopDao().getStopByID(stopId)
            stop.isFavorite = isFavorite
            database.stopDao().updateStopFavorite(stop)
            _stopsResponse.value = database.stopDao().getAllStops()
        }
    }

    /**
     * Gets a stop based on an id.
     * @param database The database to get stops from.
     * @param number The id of the stop to get.
     * @param callback lambda to run after data is gotten.
     */
    fun getStopById(database: AppDatabase, number: Int, callback: (Stop) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val stop = database.stopDao().getStopByID(number)
            callback(stop)
        }
    }

    /**
     * Converts a [Calendar] time into the format used by the Winnipeg Transit API
     * @param time The Calendar to convert
     * @return a string in the format ("2025-04-25T14:30")
     */
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

    /**
     * Checks if the user has an internet connection.
     * @param context The context to use to check if the user is online.
     * @return true if Online false otherwise.
     */
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}