package com.example.winnipegtransitoffline.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.winnipegtransitoffline.api.StopsManager
import com.example.winnipegtransitoffline.api.db.AppDatabase
import com.example.winnipegtransitoffline.mvvm.ModelViewViewModel
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.MarkerState
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

/**
 * Displays a screen showing a bus stop and busses arriving at the stop.
 * @param modifier The modifier to apply to the screen.
 * @param db The database holding the data.
 * @param stopsManager The StopsManager used to request data.
 * @param stopID The id of the stop to display information about.
 * @param mvvm The ViewModel to store requested data.
 */
@Composable
fun StopScreen(
    modifier: Modifier,
    db: AppDatabase,
    stopsManager: StopsManager,
    stopID: String,
    mvvm: ModelViewViewModel
) {
    LaunchedEffect(stopID) {
        mvvm.setStopData(stopID)
    }

    val stop = mvvm.stop_data

    var checked by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(stop) {
        if (stop != null) {
            checked = stop.isFavorite
        }
    }

    val cameraState = rememberCameraState {
        geoPoint = stop?.let {
            GeoPoint(
            stop.centre.geographic.latitude.toDouble(),
            stop.centre.geographic.longitude.toDouble()
            )
        } ?: GeoPoint(0.0, 0.0)
        zoom = 17.0
    }

    var mapProperties by remember {
        mutableStateOf(DefaultMapProperties)
    }


    // setup mapProperties in side effect
    SideEffect {
        mapProperties = mapProperties
            .copy(isTilesScaledToDpi = true)
            .copy(isAnimating = false)
            .copy(zoomButtonVisibility = ZoomButtonVisibility.NEVER)
            .copy(tileSources = TileSourceFactory.MAPNIK)
    }

    Column (
        modifier = modifier
            .background(Color.LightGray)
    ) {
        Row (modifier = Modifier.align(Alignment.End)){
            Text(
                text = "Save?",
                fontSize = TextUnit(5f, TextUnitType.Em),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    Log.i("Stop", stop.toString())
                    stopsManager.setFavoriteStop(db, stop!!.key, it)
                    checked = it
                    mvvm.stop_data!!.isFavorite = it
                },
                modifier = Modifier
            )

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25F)
                .clickable {  }
                .background(Color.Green)
                .padding(0.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Green)
                    .padding(0.dp)
                    .height(100.dp)
            ) {
                OpenStreetMap(
                    cameraState = cameraState,
                    properties = mapProperties
                ) {
                    Marker(
                        state = MarkerState(
                            GeoPoint(
                                stop?.centre?.geographic?.latitude?.toDouble() ?: 0.0,
                                stop?.centre?.geographic?.longitude?.toDouble() ?: 0.0
                            )
                        )
                    )
                }
            }
        }
        Row (
            modifier = Modifier
                .background(Color.Gray)
        ){
            Text(
                text = "Bus #",
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = "Bus route",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = "Time",
                textAlign = TextAlign.Right
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Log.i("ooh look at me", mvvm.data.toString())
            items(mvvm.data) { scheduleData ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = scheduleData.busNumber,
                        textAlign = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Text(
                        text = scheduleData.busRoute,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.6F)
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Text(
                        text = dateTimeToTimeStamp(scheduleData.estimatedTimeOfArrival),
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth(0.6F)
                    )
                }
            }
        }
    }
}

/**
 * A function to convert from time stamp (2025-04-25T14:30) to simple time (2:30 PM)
 * @param datetime The datetime to convert.
 * @return The converted time.
 */
fun dateTimeToTimeStamp(datetime: String): String {
    try {
        val time = datetime.split('T')[1].split(':')
        var hours = time[0].toInt()
        val minutes = time[1]
        var meridiem = "AM"
        if (hours >= 12) {
            meridiem = "PM"
            if (hours > 12) {
                hours %= 12
            }
        }
        return "$hours:$minutes $meridiem"
    }
    catch (e: IndexOutOfBoundsException) {
        return ""
    }
}