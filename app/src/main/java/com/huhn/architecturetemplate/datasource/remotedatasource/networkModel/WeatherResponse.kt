package com.huhn.architecturetemplate.datasource.remotedatasource.networkModel

import com.huhn.architecturetemplate.datasource.localdatasource.dbModel.DBWeather
import com.huhn.architecturetemplate.ui.WeatherUIState

data class WeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) {
    fun convertToDB() : DBWeather {
        return DBWeather(
            id = 1,
            city = this.name,
            state = "",
            country = this.sys.country,
            lat = this.coord.lat,
            lng = this.coord.lon,
            description = this.weather.first().description,
            icon = this.weather.first().icon,
            weatherStateId = this.weather.first().id.toString(),
            temp = this.main.temp ,
            feelslike = this.main.feelsLike,
            tempmax = this.main.tempMax,
            tempmin = this.main.tempMin,
            dewtemp = this.dt,
            clouds = this.clouds.all,
            sunrise = this.sys.sunrise,
            sunset = this.sys.sunset,
        )
    }
    fun convertToState() : WeatherUIState {
        return WeatherUIState(
            city = this.name,
            usState = "",
            country = this.sys.country,
            latitude = this.coord.lat.toString(),
            longitude = this.coord.lon.toString(),
            description = this.weather.first().description,
            icon = this.weather.first().icon,
            temp = this.main.temp.toString(),
            feelsLike = this.main.feelsLike.toString(),
            tempMax = this.main.tempMax.toString(),
            tempMin = this.main.tempMin.toString(),
            dewTemp = this.dt.toString(),
            clouds = this.clouds.all.toString(),
            sunrise = this.sys.sunrise.toString(),
            sunset = this.sys.sunset.toString()
        )
    }
}


