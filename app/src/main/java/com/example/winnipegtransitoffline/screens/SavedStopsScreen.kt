package com.example.winnipegtransitoffline.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.winnipegtransitoffline.api.StopsManager
import com.example.winnipegtransitoffline.api.db.AppDatabase
import com.example.winnipegtransitoffline.api.model.Stop
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.MarkerState
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

@Composable
fun SavedStopsScreen(
    modifier: Modifier = Modifier,
    stopsManager: StopsManager,
    navController: NavHostController,
    db: AppDatabase
) {
    val savedThingsList = stopsManager.stopsResponse.value

    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        modifier = modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        items(savedThingsList.filter { it.isFavorite }) {
            SavedStopCard(
                stop = it,
                stopsManager = stopsManager,
                navController = navController,
                db = db
            )
        }
    }
}

// probably shouldn't be String but will fix that when I know what it will be
@Composable
fun SavedStopCard(
    stop: Stop,
    stopsManager: StopsManager,
    navController: NavHostController,
    db: AppDatabase
) {
    var checked by remember {
        mutableStateOf(stop.isFavorite)
    }
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(
            stop.centre.geographic.latitude.toDouble(),
            stop.centre.geographic.longitude.toDouble()
        )
        zoom = 18.0
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
            .copy(isMultiTouchControls = false)
    }

    Card (
        modifier = Modifier
            .clickable {
                navController.navigate("stop/${stop.number}")
            }
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
                properties = mapProperties,
                onMapClick = {
                    navController.navigate("stop/${stop.number}")
                },
//                onMapLongClick = {}
            ) {
                Marker(
                    state = MarkerState(
                        GeoPoint(
                            stop.centre.geographic.latitude.toDouble(),
                            stop.centre.geographic.longitude.toDouble()
                        )
                    )
                )
            }
        }
        Row (
            modifier = Modifier.background(Color.Cyan)
        ){
            Text(
                text = stop.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxSize(3/4F)
            )


            // Change this to star/heart eventually
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    Log.i("Stop", stop.toString())
                    stopsManager.setFavoriteStop(db, stop.key, it)
                    checked = it
                },
                modifier = Modifier.fillMaxSize()
            )
        }

    }
}