package com.huhn.architecturetemplate.ui

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient


data class WeatherUIState(
    var isInitialized: Boolean = false,
    val city : String = "",
    val usState : String = "",
    val country : String = "",
    val latitude : String = "0.0",
    val longitude: String = "0.0",
    val description : String = "",
    val weatherStateId : String = "",
    val icon : String = "",
    val temp : String = "0.0",
    val feelsLike : String = "0.0",
    val tempMax: String = "0.0",
    val tempMin : String = "0.0",
    val dewTemp : String = "0.0",
    val clouds: String = "",
    val sunrise : String = "",
    val sunset: String = "",
    val errorMsg: String = "",
)

data class LocationState(
    val location: Location? = null,
    val locLatitude : String = "0.0",
    val locLongitude: String = "0.0",
    val fusedClient : FusedLocationProviderClient? = null,
)

sealed interface WeatherUserEvent {
    data object OnInitializeWeatherEvent : WeatherUserEvent
    data class OnShowHideDetailsChanged(val data: Boolean = false) : WeatherUserEvent
    data class OnFetchLocation(val data: Location?) : WeatherUserEvent
    data class OnCityEvent(val data: String?) : WeatherUserEvent
    data class OnUsStateEvent (val data: String?) : WeatherUserEvent
    data class OnCountryEvent (val data: String?) : WeatherUserEvent
    data class OnDisplayWeatherEvent(val isByLoc: Boolean = false) : WeatherUserEvent
    data class OnGetLocation(val context: Context) : WeatherUserEvent
    data class OnLatitudeEvent (val data: String): WeatherUserEvent
    data class OnLongitudeEvent (val data: String): WeatherUserEvent
}