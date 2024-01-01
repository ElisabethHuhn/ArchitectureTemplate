package com.huhn.architecturetemplate.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huhn.architecturetemplate.datasource.MainRepositoryImpl
import com.huhn.architecturetemplate.ui.LocationState
import com.huhn.architecturetemplate.ui.WeatherUIState
import com.huhn.architecturetemplate.ui.WeatherUserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModelImpl(
    private val repo : MainRepositoryImpl
) : ViewModel()
{
    //Define state for recomposing UI
    private val _weatherState = MutableStateFlow(WeatherUIState())
    val weatherState = _weatherState.asStateFlow()

    private val _locationState  = MutableStateFlow( LocationState())
    val locationState = _locationState.asStateFlow()

    //Define responses to User Trigger Events
    fun onWeatherUserEvent (event: WeatherUserEvent){
        when(event) {
            is WeatherUserEvent.OnInitializeWeatherEvent -> onInitialization()
            is WeatherUserEvent.OnShowHideDetailsChanged -> onInitializedChanged(event.data)
            is WeatherUserEvent.OnFetchLocation -> onLocationChanged(event.data)
            is WeatherUserEvent.OnCityEvent -> onCityChanged(event.data ?: "")
            is WeatherUserEvent.OnUsStateEvent -> onUsStateChanged(event.data ?: "")
            is WeatherUserEvent.OnCountryEvent -> onCountryChanged(event.data ?: "")
            is WeatherUserEvent.OnDisplayWeatherEvent -> onDisplayWeather(event.isByLoc)
            is WeatherUserEvent.OnGetLocation -> onGetLocation()
            is WeatherUserEvent.OnLatitudeEvent -> onLatitudeChanged(event.data )
            is WeatherUserEvent.OnLongitudeEvent -> onLongitudeChanged(event.data )
        }
    }

    //region stateUpdate

    //Define functions that update state values

    private  fun onWeatherChanged(weather: WeatherUIState){
        _weatherState.value = weather
    }

    private fun onInitializedChanged(data : Boolean){
        _weatherState.update { it.copy(isInitialized = data) }
    }
    private fun onCityChanged(data: String) {
        _weatherState.update { it.copy(city = data) }
    }
    private fun onUsStateChanged(data: String) {
        _weatherState.update { it.copy(usState = data) }
    }
    private fun onCountryChanged(data: String) {
        _weatherState.update { it.copy(country = data) }
    }
    private fun onLatitudeChanged(data: String) {
        _weatherState.update { it.copy(latitude = data) }
    }
    private fun onLongitudeChanged(data: String) {
        _weatherState.update { it.copy(longitude = data) }
    }
    fun onErrorChanged(data: String) {
        _weatherState.update { it.copy(errorMsg = data) }
    }

    private fun onLocationChanged(location: Location?) {
        onLocLatitudeChanged(data = location?.latitude.toString() )
        onLocLongitudeChanged(data = location?.longitude.toString() )
    }
    private fun onLocLatitudeChanged(data: String) {
        _locationState.update { it.copy(locLatitude = data) }
    }
    private fun onLocLongitudeChanged(data: String) {
        _locationState.update { it.copy(locLongitude = data) }
    }
    private fun onGetLocation() {
        onLatitudeChanged(data = locationState.value.locLatitude)
        onLongitudeChanged(data = locationState.value.locLongitude)
    }

    private fun onInitialization() {
        _weatherState.update { it.copy(
            city = "Atlanta",
            usState = "Georgia",
            country = "USA",
            latitude = "34.xxx",
            longitude = "-84.9999",
            description = "Nice weather today",
            icon = "some_url",
            temp = "73",
            feelsLike = "80",
            tempMax = "99",
            tempMin = "65",
            dewTemp = "70",
            clouds = "50",
            sunrise = "1000",
            sunset = "2200"
        ) }
    }

    //endregion



    //define functions that fetch data

    /*
     * Makes data source calls and updates weather state with the weatherResponse
     */
    private fun onDisplayWeather(isByLoc: Boolean) {
        viewModelScope.launch {
            val weatherResponse = repo.getDisplayWeather(
                isByLoc = isByLoc,
                weatherState = weatherState.value
            )
            weatherResponse?.let { onWeatherChanged(it) }
        }
    }
}
