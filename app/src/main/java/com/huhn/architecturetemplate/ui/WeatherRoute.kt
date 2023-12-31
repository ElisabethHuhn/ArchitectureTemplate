package com.huhn.architecturetemplate.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.huhn.architecturetemplate.R
import com.huhn.architecturetemplate.viewmodel.WeatherViewModelImpl
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherRoute(
    screenTitle: Int,
) {
    val viewModel : WeatherViewModelImpl = koinViewModel()
    val state by viewModel.weatherState.collectAsStateWithLifecycle()

    //preload weather from last time the app ran
    LaunchedEffect(key1 = "firsttime" ){
        viewModel.onWeatherUserEvent(WeatherUserEvent.OnDisplayWeatherEvent(false))
   }

    WeatherScreen (
        screenTitle = screenTitle,
        state = state,
        onUserEvent = viewModel::onWeatherUserEvent,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun WeatherScreen(
    screenTitle : Int,
    state: WeatherUIState,
    onUserEvent: (WeatherUserEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(screenTitle),
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
            )
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                content = { Text(text = "FAB")},
//                onClick = {
//                    /*TODO Do something here if have FAB */
//
//                },
//                shape = RectangleShape,
//            )
//        }

    ) {it

        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(95.0.dp))

            //city row
            Row (
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1.0f)
                        .semantics { this.contentDescription = "city field" },
                    value = state.city,
                    onValueChange = { enteredText ->
                        onUserEvent(WeatherUserEvent.OnCityEvent(enteredText))
                        keyboardController?.hide()
                    },
                    label = { Text("City") },
                )
                Spacer(modifier = Modifier.width(5.dp))
                TextField(
                    modifier = Modifier.weight(.8f),
                    value = state.usState,
                    onValueChange = { enteredText ->
                        onUserEvent(WeatherUserEvent.OnUsStateEvent(enteredText))
                        keyboardController?.hide()
                    },
                    label = { Text("US State") }
                )
                Spacer(modifier = Modifier.width(5.dp))
                TextField(
                    modifier = Modifier.weight(.8f),
                    value = state.country,
                    onValueChange = { enteredText ->
                        onUserEvent(WeatherUserEvent.OnCountryEvent(enteredText))
                        keyboardController?.hide()
                    },
                    label = { Text("Country") }
                )
            }

            val textMsg = state.errorMsg
            Text(
                modifier = Modifier
                    .testTag("error_field")
                    .semantics { this.contentDescription = "error field" },
                text = textMsg
            )

            Button(
                onClick = {
                    onUserEvent(WeatherUserEvent.OnDisplayWeatherEvent(isByLoc = false))
                    onUserEvent(WeatherUserEvent.OnShowHideDetailsChanged(data = true))
                })
            {
                Text(text = "Get Weather by City")
            }


            //location row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = state.latitude,
                    onValueChange = { enteredText ->
                        onUserEvent(WeatherUserEvent.OnLatitudeEvent(enteredText))
                        keyboardController?.hide()
                    },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
                Spacer(modifier = Modifier.width(5.dp))
                TextField(
                    modifier = Modifier.weight(1f),
                    value = state.longitude,
                    onValueChange = { enteredText ->
                        onUserEvent(WeatherUserEvent.OnLongitudeEvent(enteredText))
                        keyboardController?.hide()
                    },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
            }
            Button(
                onClick = {
                    onUserEvent(WeatherUserEvent.OnDisplayWeatherEvent(isByLoc = true))
                    onUserEvent(WeatherUserEvent.OnShowHideDetailsChanged(data = true))
                })
            {
                Text(text = "Get Weather by Location")
            }

            Button(
                onClick = {
                    onUserEvent(WeatherUserEvent.OnGetLocation)
                    onUserEvent(WeatherUserEvent.OnDisplayWeatherEvent(isByLoc = true))
                    onUserEvent(WeatherUserEvent.OnShowHideDetailsChanged(data = true))
                })
            {
                Text(text = "Current Location")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${state.icon}@2x.png",
                    placeholder = painterResource(id = R.drawable.ic_android_black_24dp),
                    error = painterResource(id = R.drawable.ic_android_black_24dp),
                    contentDescription = "An icon suggesting weather description",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            BorderStroke(5.dp, Color.Gray),
                            RectangleShape
                        )
                )
                Spacer(modifier = Modifier.width(5.0.dp))
                Text(
                    modifier = Modifier
                        .testTag("description_field"),
                    text = state.description
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val temperatureInC = (state.temp.toDouble() - 273.15).toInt().toString()
                Text(text = "Temperature:", fontWeight = FontWeight.Bold)
                Text(text = "$temperatureInC C")
                Spacer(modifier = Modifier.width(5.0.dp))
                Text(text = "Feels Like:", fontWeight = FontWeight.Bold)
                Text(text = state.feelsLike, modifier = Modifier)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                    Text(text = "Max:", fontWeight = FontWeight.Bold)
                    Text(text = state.tempMax, modifier = Modifier)
                    Spacer(modifier = Modifier.width(5.0.dp))
                    Text(text = "Min:", fontWeight = FontWeight.Bold)
                    Text(text = state.tempMin, modifier = Modifier)
                }

        }
    }
}
