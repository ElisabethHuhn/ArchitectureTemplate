package com.huhn.architecturetemplate.ui

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient


data class WeatherUIState(
    val isRefreshing: Boolean = false,
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
    val calcTime : String = "0.0",
    val clouds: String = "",
    val sunrise : String = "",
    val sunset: String = "",
    val errorMsg: String = "",
    val windDir : String = "",
    val windSpeed : String = "",
    val forecastState : List <ForecastUIState> = emptyList(),
)

data class ForecastUIState(
    val dt : String = "",
    val dt_txt : String = "",
    var description : String = "",
    val weatherStateId : String = "",
    var icon : String = "",
    val temp : String = "0.0",
    val feelsLike : String = "0.0",
    var tempMax: String = "0.0",
    var tempMin : String = "0.0",
    val calcTime : String = "0.0",
    val clouds: String = "",
    val sunrise : String = "",
    val sunset: String = "",
    val errorMsg: String = "",
    val windDir : String = "",
    val windSpeed : String = "",
)

//data class LocationState(
//    val location: Location? = null,
//    val locLatitude : String = "0.0",
//    val locLongitude: String = "0.0",
//    val fusedClient : FusedLocationProviderClient? = null,
//)

sealed interface WeatherUserEvent {
    data object OnInitializeWeatherEvent : WeatherUserEvent
    data class OnDisplayWeatherEvent(val isByLoc: Boolean = false, val isForecast : Boolean = false) : WeatherUserEvent
    data class OnDisplayLandingEvent(val isByLoc: Boolean = false, val isForecast : Boolean = false) : WeatherUserEvent
    data class OnDisplayForecastEvent(val isByLoc: Boolean = false, val isForecast : Boolean = false) : WeatherUserEvent
    data class OnShowHideDetailsChanged(val data: Boolean = false) : WeatherUserEvent
//    data class OnFetchLocation(val data: Location?) : WeatherUserEvent
    data class OnCityEvent(val data: String?) : WeatherUserEvent
    data class OnUsStateEvent (val data: String?) : WeatherUserEvent
    data class OnCountryEvent (val data: String?) : WeatherUserEvent
    data class OnGetLocation(val context: Context) : WeatherUserEvent
    data class OnLatitudeEvent (val data: String): WeatherUserEvent
    data class OnLongitudeEvent (val data: String): WeatherUserEvent
}