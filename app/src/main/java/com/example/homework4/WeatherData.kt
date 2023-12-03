package com.example.homework4

data class WeatherData(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
)

data class Current(
    val temperatureByCELSIUS: Double,
    val temperatureByFAHRENHEIT: Double,
    val condition: Condition,
)

data class Condition(
    val text: String,
    val code: Int
)
