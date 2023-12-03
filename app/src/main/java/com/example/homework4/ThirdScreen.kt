package com.example.homework4

import android.icu.util.MeasureUnit
import android.icu.util.MeasureUnit.CELSIUS
import android.icu.util.MeasureUnit.FAHRENHEIT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.homework4.Settings.temperatureType

@Composable
fun Set(navController: NavController) {
    val temperatureUnit by temperatureType.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Settings", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Temperature Unit", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))

        TemperatureOption(CELSIUS, "Celsius", temperatureUnit)
        Spacer(modifier = Modifier.height(4.dp))

        TemperatureOption(FAHRENHEIT, "Fahrenheit", temperatureUnit)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("welcome_screen") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Go Back to Welcome Screen")
        }
    }
}

@Composable
fun TemperatureOption(unit: MeasureUnit, label: String, selectedUnit: MeasureUnit?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            RadioButton(
                selected = selectedUnit == unit,
                onClick = {
                    temperatureType.postValue(unit)
                }
            )
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}