package com.example.winnipegtransitoffline.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.winnipegtransitoffline.api.StopsManager
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.MarkerState
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import org.osmdroid.util.GeoPoint

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    stopsManager: StopsManager
) {
    var searchLocation by remember {
        mutableStateOf("")
    }
    val resultList = remember {
        mutableListOf("")
    }
    var locationDropdown by remember {
        mutableStateOf(false)
    }
    
    val stops = stopsManager.stopsResponse

    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(
            49.89,
            -97.14
        )
        zoom = 14.0
    }

    var mapProperties by remember {
        mutableStateOf(DefaultMapProperties)
    }

    Column (
        modifier = modifier
    ){
        TextField("This is just here so I can shift focus", {})
        Box {
            OutlinedTextField(
                value = searchLocation,
                onValueChange = { searchLocation = it },
                label = {
                    Text(text = "Search")
                },
                placeholder = {
                    Text(text = "Red River Polytechnic")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        locationDropdown = it.isFocused
                        resultList.add("ooh scary text")
                    }
            )
            DropdownMenu(
                expanded = locationDropdown,
                onDismissRequest = {
                    locationDropdown = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                for (it in resultList.takeLast(5))
                DropdownMenuItem(
                    text = {
                        Text(text = it)
                    },
                    onClick = {
                        locationDropdown = false
                        searchLocation = it
                    }
                )
            }
        }
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
                for (stop in stops.value) {
                    Log.i("stop marker", stop.toString())
                    Marker(
                        state = MarkerState(
                            GeoPoint(
                                stop.centre.geographic.latitude.toDouble(),
                                stop.centre.geographic.longitude.toDouble()
                            )
                        )
                    ) {
                        Row (
                            modifier = Modifier
                                .background(Color.White)
                                .clickable {
                                    navController.navigate("stop/${stop.number}")
                                }
                        ){
                            Text(text = stop.name)
                        }
                    }
                }
            }
        }
    }
}

