package com.example.winnipegtransitoffline.screens

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import java.util.Date
import java.util.Locale


/*
 *
 *
 */
@Composable
fun PlanRouteScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var startLocation by remember {
        mutableStateOf("")
    }
    var endLocation by remember {
        mutableStateOf("")
    }
    var buttonText by remember {
        mutableStateOf("Arrive Before")
    }
    var timeDropdown by remember {
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableStateOf<Long?>(null)
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5F)
                .fillMaxWidth()

                .background(Color.Green)
                .padding(0.dp)
        ) {
            Text(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
                text = "Map Here"
            )
        }

        OutlinedTextField(
            value = startLocation,
            onValueChange = { startLocation = it },
            label = {
                Text(text = "Start Location")
            },
            placeholder = {
                Text(text = "Red River Polytechnic")
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = endLocation,
            onValueChange = { endLocation = it },
            label = {
                Text(text = "End Location")
            },
            placeholder = {
                Text(text = "University of Manitoba")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            OutlinedTextField(
                value = buttonText,
                onValueChange = { buttonText = it },
                label = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth(0.5F)
                    .clickable { timeDropdown = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

            DropdownMenu(
                expanded = timeDropdown,
                onDismissRequest = {
                    timeDropdown = false
                },
                modifier = Modifier.fillMaxWidth(0.5F)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Arrive Before")
                    },
                    onClick = {
                        buttonText = "Arrive Before"
                        timeDropdown = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "Leave After")
                    },
                    onClick = {
                        buttonText = "Leave After"
                        timeDropdown = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "Leave Before")
                    },
                    onClick = {
                        buttonText = "Leave Before"
                        timeDropdown = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "Arrive After")
                    },
                    onClick = {
                        buttonText = "Arrive After"
                        timeDropdown = false
                    }
                )
            }

            // TODO: this should probably be a datetime picker but later problems
            DatePickerDocked(
                onDismiss = {
                    selectedDate = it
                    Log.i("NWF", selectedDate.toString())
                }
            )

        }
        Button(
            onClick = {navController.navigate("route/1")},
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp)
                .height(OutlinedTextFieldDefaults.MinHeight)
        ) {
            Text(text = "Plan Route")
        }
    }
}

// Docked Date Picker by:
// https://developer.android.com/develop/ui/compose/components/datepickers
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    onDismiss: (Long?) -> Unit
) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text("Time") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = {
                    showDatePicker = false
                    onDismiss(datePickerState.selectedDateMillis)
                },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    // + 86_400_000 to display date chosen not date before
    // 86_400_000 milliseconds in a day
    return SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date(millis + 86_400_000))
}