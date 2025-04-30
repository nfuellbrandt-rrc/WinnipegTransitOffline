package com.example.winnipegtransitoffline.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RouteScreen(
    modifier: Modifier = Modifier
) {
    val busses = listOf("Portage-Kildonan", "Rothesay")
    Column (
        modifier = modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25F)
                .clickable { }
                .background(Color.Green)
                .padding(0.dp)
        ) {
            Text(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Map Here"
            )
        }
        // TODO: Add styling after I figure out what input looks like
        LazyColumn {
            items(listOf("", "", "")) {
                Card (
                    shape = RoundedCornerShape(15.dp)
                ){
                    Text(
                        text = "Going to put things here not quite sure what it will look like though",
                        modifier = Modifier.padding(15.dp, 5.dp)
                    )
                }
            }
        }
    }
}