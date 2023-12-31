package com.huhn.architecturetemplate.datasource.localdatasource.dbModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.huhn.architecturetemplate.ui.WeatherUIState

@Entity
data class DBWeather(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lng") val lng: Double,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "weatherStateId") val weatherStateId: String,
    @ColumnInfo(name = "temp") val temp: Double,
    @ColumnInfo(name = "feelslike") val feelslike: Double,
    @ColumnInfo(name = "tempmax") val tempmax: Double,
    @ColumnInfo(name = "tempmin") val tempmin: Double,
    @ColumnInfo(name = "dewtemp") val dewtemp: Int,
    @ColumnInfo(name = "clouds") val clouds: Int,
    @ColumnInfo(name = "sunrise") val sunrise: Int,
    @ColumnInfo(name = "sunset") val sunset: Int,
)
{
    fun convertToState() : WeatherUIState {
        return WeatherUIState(
            city = this.city,
            usState = this.state,
            country = this.country,
            latitude = this.lat.toString(),
            longitude = this.lng.toString(),
            description = this.description,
            icon = this.icon,
            temp = this.temp.toString(),
            feelsLike = this.feelslike.toString(),
            tempMax = this.tempmax.toString(),
            tempMin = this.tempmin.toString(),
            dewTemp = this.dewtemp.toString(),
            clouds = this.clouds.toString(),
            sunrise = this.sunrise.toString(),
            sunset = this.sunset.toString()
        )
    }
}