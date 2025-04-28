package com.example.winnipegtransitoffline.destinations

sealed class Destination(val route: String) {
    object SavedStops : Destination("saved_stops")
    object PlanRoute : Destination("plan_route")
    object Search : Destination("search")
    object Route : Destination("route/{routeID}") {
        fun createRoute(routeID: String?) = "route/$routeID"
    }
    object Stop : Destination("stop/{stopID}") {
        fun createRoute(stopID: Int?) = "stop/$stopID"
    }
}