package com.example.winnipegtransitoffline.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.winnipegtransitoffline.destinations.Destination

/**
 * A navigation bar displayed at the bottom of the screen.
 * @param navController The navController used to navigate to each page.
 */
@Composable
fun BottomNav(navController: NavController) {
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntry
        val currentDestination = navBackStackEntry?.destination


//        val ic_plan_route = painterResource(id = R.drawable.ic_plan_route)

        NavigationBarItem(
            selected = currentDestination?.route == Destination.SavedStops.route,
            onClick = { navController.navigate(Destination.SavedStops.route) {
                popUpTo(Destination.SavedStops.route)
                launchSingleTop = true
            }},
            icon = { Icon(imageVector = Icons.Default.Star, contentDescription = "Saved") },
            label = { Text(text = "Saved")}
        )
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Search.route,
            onClick = { navController.navigate(Destination.Search.route) {
                popUpTo(Destination.Search.route)
                launchSingleTop = true
            }},
            icon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
            label = { Text(text = Destination.Search.route)}
        )
//        NavigationBarItem(
//            selected = currentDestination?.route == Destination.PlanRoute.route,
//            onClick = { navController.navigate(Destination.PlanRoute.route) {
//                popUpTo(Destination.PlanRoute.route)
//                launchSingleTop = true
//            }},
//            icon = { Icon(painter =  ic_plan_route, contentDescription = null)},
//            label = { Text(text = "plan route")}
//        )
    }
}