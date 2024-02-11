package com.huhn.architecturetemplate.datasource.remotedatasource.networkModel

import com.huhn.architecturetemplate.datasource.localdatasource.dbModel.DBWeather
import com.huhn.architecturetemplate.ui.ForecastUIState
import com.huhn.architecturetemplate.ui.WeatherUIState
import com.huhn.architecturetemplate.utils.convertEpochToDaysSinceEpoch

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
    fun convertToState(position : Int, isTodayOnly : Boolean) : WeatherUIState {
        val forecastUIList = mutableListOf<ForecastUIState>()
        val forecastSummaryList = mutableListOf<DaySummary>()

        val today = convertEpochToDaysSinceEpoch(System.currentTimeMillis())
        this.list?.forEach { forecast ->
            val day = convertEpochToDaysSinceEpoch(forecast.dt.toLong() * 1000)
            if ( !isTodayOnly ||
                ((isTodayOnly) && (day == today)))
            {
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

                if (!isTodayOnly) {

                    //update the summary list
                    //find the summary entry for this day
                    var summaryEntry = forecastSummaryList.find { it.daySinceEpoch == day }
                    var temp: Int
                    try {
                        temp = forecast.main.temp.toInt()
                    } catch (e: Exception) {
                        temp = 0

                    }
                    if (summaryEntry == null) {
                        //create a new summary entry
                        summaryEntry = DaySummary(
                            daySinceEpoch = day,
                            icons = mutableMapOf(forecastItem.icon to 1),
                            highTemp = temp,
                            lowTemp = temp,
                            description = mutableMapOf(forecastItem.description to 1)
                        )
                        forecastSummaryList.add(summaryEntry)
                    } else {
                        //update the summary entry
                        var newCount = (summaryEntry.icons[forecastItem.icon] ?: 0) + 1
                        summaryEntry.icons.put(forecastItem.icon, newCount) //update the count

                        summaryEntry.highTemp =
                            if (temp > summaryEntry.highTemp)
                                temp
                            else
                                summaryEntry.highTemp

                        summaryEntry.lowTemp =
                            if (temp < summaryEntry.lowTemp)
                                temp
                            else
                                summaryEntry.lowTemp

                        newCount = (summaryEntry.description[forecastItem.description] ?: 0) + 1
                        summaryEntry.description.put(
                            forecastItem.description,
                            newCount
                        ) //update the count
                    }
                }
            }
        }
        val newForecastList = mutableListOf<ForecastUIState>()
        if (!isTodayOnly) {
            val alreadySeenDay = mutableListOf<Long>()
            //only retain one entry per day, with the max's from the summary
            forecastUIList.forEach() { origForecastState ->
                val day = convertEpochToDaysSinceEpoch(origForecastState.dt.toLong() * 1000)
                if (!alreadySeenDay.contains(day)) {
                    alreadySeenDay.add(day)
                    if (forecastSummaryList.isNotEmpty()) {
                        val summaryEntry = forecastSummaryList.find { it.daySinceEpoch == day }
                        if (summaryEntry != null) {
                            val newForecastState = origForecastState.copy()

                            //get icon with max count from summaryEntry
                            val icon = summaryEntry.icons.maxByOrNull { it.value }?.key ?: ""
                            newForecastState.icon = icon

                            //get description with max count from summaryEntry
                            val description =
                                summaryEntry.description.maxByOrNull { it.value }?.key ?: ""
                            newForecastState.description = description

                            newForecastState.tempMax = summaryEntry.highTemp.toString()
                            newForecastState.tempMin = summaryEntry.lowTemp.toString()

                            newForecastList.add(newForecastState)
                        }
                    }
                }
            }
        }

        val uiForecastList = if (isTodayOnly) forecastUIList else newForecastList

        return WeatherUIState(
            isRefreshing = false,
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
            forecastState = uiForecastList
        )
    }
}

data class DaySummary (
    val daySinceEpoch: Long,
    val icons: MutableMap<String, Int>,  //icon, count
    var highTemp: Int,
    var lowTemp: Int,
    val description: MutableMap<String, Int>, //description, count
)
