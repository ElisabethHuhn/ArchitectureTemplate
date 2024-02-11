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
            feelslike = this.main.feels_like,
            tempmax = this.main.temp_max,
            tempmin = this.main.temp_min,
            calcTime = this.dt,
            clouds = this.clouds.all,
            sunrise = this.sys.sunrise,
            sunset = this.sys.sunset,
            windDir = this.wind.deg,
            windSpeed = this.wind.speed
        )
    }
    fun convertToState() : WeatherUIState {
        val windDirCompass = when (this.wind.deg) {
            in 0..22 ->  "N"
            in 23..67 ->  "NE"
            in 68..112 ->  "E"
            in 113..157 ->  "SE"
            in 158..202 ->  "S"
            in 203..247 ->  "SW"
            in 248..292 ->  "W"
            in 293..337 ->  "NW"
            in 338..360 ->  "N"
            else -> "N"
        }
        return WeatherUIState(
            isRefreshing = false,
            city = this.name,
            usState = "",
            country = this.sys.country,
            latitude = this.coord.lat.toString(),
            longitude = this.coord.lon.toString(),
            description = this.weather.first().description,
            icon = this.weather.first().icon,
            temp = this.main.temp.toString(),
            feelsLike = this.main.feels_like.toString(),
            tempMax = this.main.temp_max.toString(),
            tempMin = this.main.temp_min.toString(),
            calcTime = this.dt.toString(),
            clouds = this.clouds.all.toString(),
            sunrise = this.sys.sunrise.toString(),
            sunset = this.sys.sunset.toString(),
            windDir = windDirCompass,
            windSpeed = this.wind.speed.toString()
        )
    }
}


