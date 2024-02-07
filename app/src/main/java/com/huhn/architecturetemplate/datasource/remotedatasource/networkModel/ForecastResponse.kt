package com.huhn.architecturetemplate.datasource.remotedatasource.networkModel

import com.huhn.architecturetemplate.datasource.localdatasource.dbModel.DBWeather
import com.huhn.architecturetemplate.ui.ForecastUIState
import com.huhn.architecturetemplate.ui.WeatherUIState

data class ForecastResponse (
    val city: City?,
    val cnt: Int,
    val cod: String?,
    val list: List<Forecast>?,
    val message: Double
)
{
    fun convertToDB() : DBWeather {
        //This is a dubious conversion as it only uses the first forecast object
        return DBWeather(
            id = 1,
            city = this.city?.name ?: "No City Name",
            state = "",
            country = this.city?.country ?: "No Country Name",
            lat = this.city?.coord?.lat ?: 0.0,
            lng = this.city?.coord?.lon ?: 0.0,
            description = this.list?.first()?.weather?.first()?.description ?: "No Description",
            icon = this.list?.first()?.weather?.first()?.icon ?:"" ,
            weatherStateId = this.list?.first()?.weather?.first()?.id.toString(),
            temp = this.list?.first()?.main?.temp ?: 0.0,
            feelslike = this.list?.first()?.main?.feels_like ?: 0.0,
            tempmax = this.list?.first()?.main?.temp_max ?: 0.0,
            tempmin = this.list?.first()?.main?.temp_min ?: 0.0,
            calcTime = this.list?.first()?.dt ?: 0,
            clouds = this.list?.first()?.clouds?.all ?: 0,
            sunrise = this.list?.first()?.sys?.sunrise ?: 0,
            sunset = this.list?.first()?.sys?.sunset ?: 0,
            windDir = this.list?.first()?.wind?.deg ?: 0,
            windSpeed = this.list?.first()?.wind?.speed ?: 0.0
        )
    }
    fun convertCompassHeading(degrees: Int) : String {
        return when (degrees) {
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
    }
    fun convertToState(position : Int) : WeatherUIState {
        val forecastUIList = mutableListOf<ForecastUIState>()
        this.list?.forEach { forecast ->
            val forecastItem =  ForecastUIState(
                dt = forecast.dt.toString(),
                dt_txt = forecast.dt_txt,
                description = forecast.weather.first().description,
                icon = forecast.weather.first().icon,
                temp = forecast.main.temp.toString(),
                feelsLike = forecast.main.feels_like.toString(),
                tempMax = forecast.main.temp_max.toString(),
                tempMin = forecast.main.temp_min.toString(),
                calcTime = forecast.dt.toString(),
                clouds = forecast.clouds.all.toString(),
                sunrise = forecast.sys.sunrise.toString(),
                sunset = forecast.sys.sunset.toString(),
                windDir = convertCompassHeading(forecast.wind.deg),
                windSpeed = forecast.wind.speed.toString()
            )
            forecastUIList.add(forecastItem)
        }

        return WeatherUIState(
            city = this.city?.name ?: "No City Name",
            usState = "",
            country = this.city?.country ?: "No Country Name",
            latitude = this.city?.coord?.lat.toString() ,
            longitude = this.city?.coord?.lon.toString(),
            description = this.list?.get(position)?.weather?.first()?.description ?: "No Description",
            icon = this.list?.get(position)?.weather?.first()?.icon ?:"" ,
            temp = this.list?.get(position)?.main?.temp.toString(),
            feelsLike = this.list?.get(position)?.main?.feels_like.toString(),
            tempMax = this.list?.get(position)?.main?.temp_max.toString(),
            tempMin = this.list?.get(position)?.main?.temp_min.toString(),
            calcTime = this.list?.get(position)?.dt.toString(),
            clouds = this.list?.get(position)?.clouds?.all.toString(),
            sunrise = this.list?.get(position)?.sys?.sunrise.toString(),
            sunset = this.list?.get(position)?.sys?.sunset.toString(),
            windDir = convertCompassHeading(this.list?.get(position)?.wind?.deg ?: 0),
            windSpeed = this.list?.get(position)?.wind?.speed.toString(),
            forecastState = forecastUIList
        )
    }
}
