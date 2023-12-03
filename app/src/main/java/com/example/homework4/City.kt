package com.example.homework4

import android.icu.util.MeasureUnit.CELSIUS
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class City(
    val name: String,
    val description: String,
    val imageResource: Int
)

@Composable
fun City(city: City, viewModel: WeatherViewModel) {
    val weatherData = viewModel.weatherData.observeAsState()
    val temperatureType = Settings.temperatureType.observeAsState()

    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text(
                    text = city.name, fontWeight = FontWeight.Bold, fontSize = 18.sp
                )
                    Image(
                        painter = painterResource(id = city.imageResource),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(shape = RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                    weatherData.value?.let {
                        val temp = if (temperatureType.value == CELSIUS) "${it.current.temperatureByCELSIUS}°C" else "${it.current.temperatureByFAHRENHEIT}°F"
                        Text(
                            text = "Temperature: $temp",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Text(
                        text = city.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
            }
        }
    }