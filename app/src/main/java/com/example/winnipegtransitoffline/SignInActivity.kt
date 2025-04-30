package com.example.winnipegtransitoffline

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.winnipegtransitoffline.screens.SignInScreen
import com.example.winnipegtransitoffline.ui.theme.WinnipegTransitOfflineTheme

/**
 * Used to display SignInScreen
 * @see SignInScreen
 */
class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WinnipegTransitOfflineTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context: Context = applicationContext
                    SignInScreen(context = context, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}