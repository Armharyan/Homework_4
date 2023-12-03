package com.example.homework4

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherData>()
    private val _currentWeatherData = MutableLiveData<WeatherData>()

    val weatherData: LiveData<WeatherData> = _weatherData
    val currentWeatherData: LiveData<WeatherData> = _currentWeatherData

    fun fetchWeatherData(city: City, apiKey: String) {
        viewModelScope.launch {
            try {
                val weather = weatherApiService.getWeather(city.name, apiKey)
                _weatherData.postValue(weather)
            } catch (e: Exception) {
                _weatherData.postValue(null)
                Log.e("WeatherViewModel", "Error fetching weather data: $e")
            }
        }
    }

    fun fetchWeatherForLocation(
        context: Context,
        weatherApiService: WeatherApiService,
        apiKey: String
    ) {
        try {
            if (hasLocationPermission(context)) {
                viewModelScope.launch {
                    var location = LocationProvider.getCurrentLocation()
                    if (location == null) {
                        delay(6000L)
                        location = LocationProvider.getCurrentLocation()
                    }

                    Log.e("WEATHER", "Pre-Request location: $location")

                    if (location != null) {
                        val cityName = "${location.first},${location.second}"
                        val weather = weatherApiService.getWeather(cityName, apiKey)
                        _currentWeatherData.postValue(weather)
                    } else {
                        _currentWeatherData.postValue(null)
                    }
                }
            } else {
                _currentWeatherData.postValue(null)
            }
        } catch (e: Exception) {
            _currentWeatherData.postValue(null)
            Log.e("WeatherViewModel", "Error fetching weather data for location: $e")
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}