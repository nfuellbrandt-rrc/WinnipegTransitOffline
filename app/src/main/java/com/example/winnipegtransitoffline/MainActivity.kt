package com.example.winnipegtransitoffline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.winnipegtransitoffline.api.StopsManager
import com.example.winnipegtransitoffline.api.db.AppDatabase
import com.example.winnipegtransitoffline.api.model.StopSchedule
import com.example.winnipegtransitoffline.destinations.Destination
import com.example.winnipegtransitoffline.mvvm.ModelViewViewModel
import com.example.winnipegtransitoffline.navigation.BottomNav
import com.example.winnipegtransitoffline.screens.SavedStopsScreen
import com.example.winnipegtransitoffline.screens.SearchScreen
import com.example.winnipegtransitoffline.screens.StopScreen
import com.example.winnipegtransitoffline.ui.theme.WinnipegTransitOfflineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WinnipegTransitOfflineTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val db = AppDatabase.getInstance(applicationContext)
                    val stopsManager = StopsManager(db)
                    val mvvm = ModelViewViewModel(db, stopsManager, context = LocalContext.current)
                    TransitApp(
                        navController = navController,
                        stopsManager = stopsManager,
                        db = db,
                        modifier = Modifier.padding(innerPadding),
                        mvvm = mvvm
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitApp(
    navController: NavHostController,
    stopsManager: StopsManager,
    db: AppDatabase,
    modifier: Modifier = Modifier,
    mvvm: ModelViewViewModel
) {
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Offline Transit App"
                    )
                }
            )
        },
        bottomBar = { BottomNav(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Destination.SavedStops.route
        ) {
            composable(Destination.SavedStops.route) {
                SavedStopsScreen(
                    modifier = Modifier.padding(paddingValues),
                    stopsManager = stopsManager,
                    navController = navController,
                    db = db
                )
            }
            composable(Destination.Search.route) {
                SearchScreen(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController,
                    stopsManager = stopsManager
                )
            }
//            composable(Destination.PlanRoute.route) {
//                PlanRouteScreen(
//                    modifier = Modifier.padding(paddingValues),
//                    navController = navController
//                )
//            }
            composable(Destination.Stop.route) { navBackStackEntry ->
                var stop by remember {
                    mutableStateOf<StopSchedule?>(null)
                }
                val stopID: String? = navBackStackEntry.arguments?.getString("stopID")
                StopScreen(
                    modifier = Modifier.padding(paddingValues),
                    db = db,
                    stopsManager = stopsManager,
                    stopID = stopID!!,
                    mvvm = mvvm
                )
            }
//            composable(Destination.Route.route) {
//                RouteScreen(
//                    modifier = Modifier.padding(paddingValues)
//                )
//            }
        }
    }
}