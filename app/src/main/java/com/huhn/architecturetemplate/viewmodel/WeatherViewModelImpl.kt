package com.huhn.architecturetemplate.viewmodel

import androidx.lifecycle.ViewModel
import com.huhn.architecturetemplate.datasource.WeatherRepositoryImpl
import com.huhn.architecturetemplate.ui.WeatherUserEvent
import kotlinx.coroutines.flow.asStateFlow


class WeatherViewModelImpl(private val repo : WeatherRepositoryImpl) : ViewModel()
{
    //Define state for recomposing UI
    val weatherState = repo.weatherState.asStateFlow()

    //Define responses to User Trigger Events
    fun onWeatherUserEvent (event: WeatherUserEvent){
        when(event) {
            is WeatherUserEvent.OnInitializeWeatherEvent -> repo.onInitialization()
            is WeatherUserEvent.OnShowHideDetailsChanged -> repo.onInitializedChanged(event.data)
            is WeatherUserEvent.OnFetchLocation -> repo.onLocationChanged(event.data)
            is WeatherUserEvent.OnCityEvent -> repo.onCityChanged(event.data ?: "")
            is WeatherUserEvent.OnUsStateEvent -> repo.onUsStateChanged(event.data ?: "")
            is WeatherUserEvent.OnCountryEvent -> repo.onCountryChanged(event.data ?: "")
            is WeatherUserEvent.OnDisplayWeatherEvent -> repo.onDisplayWeather(event.isByLoc)
            is WeatherUserEvent.OnGetLocation -> repo.onGetLocation()
            is WeatherUserEvent.OnLatitudeEvent -> repo.onLatitudeChanged(event.data )
            is WeatherUserEvent.OnLongitudeEvent -> repo.onLongitudeChanged(event.data )
        }
    }

    //Define functions that update state values


    //define functions that fetch remote data

}
