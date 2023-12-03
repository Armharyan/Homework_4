package com.example.homework4

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.icu.util.MeasureUnit.CELSIUS
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData

object Settings {
    val temperatureType = MutableLiveData(CELSIUS)
}

class MainActivity : ComponentActivity() {
    private val locationPermissionRequestCode = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    requestLocationPermission()
                    LocationProvider.initialize(this)
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "welcome_screen"
                    ) {
                        val viewModel = WeatherViewModel()
                        viewModel.fetchWeatherForLocation(
                            this@MainActivity,
                            weatherApiService,
                            "6339a15949794fddb2d201051232411"
                        )
                        composable("welcome_screen") { WelcomeScreen(navController, viewModel)}
                        composable("second_screen") { SecondScreen(navController, viewModel)}
                        composable("settings_screen") { Set(navController)}
                    }
                }
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavController, viewModel: WeatherViewModel) {
    val temperatureType = Settings.temperatureType.observeAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        Button(
            onClick = { navController.navigate("settings_screen") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Settings Screen")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val weatherData by viewModel.currentWeatherData.observeAsState()

        weatherData?.let {
            val temp = if (temperatureType.value == CELSIUS) "${it.current.temperatureByCELSIUS}°C" else "${it.current.temperatureByFAHRENHEIT}°F"
            Text(text = "Temperature here is: $temp", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Text( text = "Welcome to the App!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Button(
            onClick = { navController.navigate("second_screen") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Go to Second Screen")
        }
    }
}

@Composable
fun SecondScreen(navController: NavController, viewModel: WeatherViewModel) {
    val cities = listOf(
        City("Yerevan", "Capital of Armenia", R.drawable.yerevan),
        City("Washington", "Capital of the United States", R.drawable.washington),
        City("Madrid", "Capital of Spain", R.drawable.madrid)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(cities) { city ->
                City(
                    city = city,
                    viewModel = viewModel
                )
            }
            item{
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(8.dp))
                {
                    Text("Go Back to Welcome Screen")
                }
            }
        }
    }
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.weatherapi.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val weatherApiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)

typealias LocationCoordinate = Pair<Double, Double>

object LocationProvider {
    private var currentLocation: LocationCoordinate? = null
    fun getCurrentLocation(): LocationCoordinate? {
        return currentLocation
    }

    @SuppressLint("MissingPermission")
    fun initialize(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 5f) {
            it.longitude = 180 + it.longitude
            currentLocation = it.longitude to it.latitude
        }
    }
}