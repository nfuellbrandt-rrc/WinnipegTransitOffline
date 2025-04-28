package com.example.winnipegtransitoffline.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {  }
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
    }
}

