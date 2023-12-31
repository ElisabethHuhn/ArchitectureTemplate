package com.huhn.architecturetemplate.datasource.remotedatasource.networkModel

import com.huhn.architecturetemplate.datasource.localdatasource.dbModel.DBWeather

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
}


