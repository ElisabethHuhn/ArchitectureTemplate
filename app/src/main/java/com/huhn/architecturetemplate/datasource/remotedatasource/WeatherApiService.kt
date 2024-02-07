package com.huhn.architecturetemplate.datasource.remotedatasource

// Retrofit interface
import com.huhn.architecturetemplate.BuildConfig
import com.huhn.architecturetemplate.datasource.remotedatasource.networkModel.ForecastResponse
import com.huhn.architecturetemplate.datasource.remotedatasource.networkModel.WeatherResponse

import retrofit2.http.GET
import retrofit2.http.Query

const val appKey = BuildConfig.weatherApiKey

interface WeatherApiService {
    //The secret key must be fetched from BuildConfig, but it is stored in the local properties file
    //The Google Gradle Secrets library moves it from local.properties to the BuildConfig
    //https://github.com/google/secrets-gradle-plugin

    @GET("weather")
    suspend fun fetchWeatherCity(
        @Query("q") cityLocation : String,
        @Query("units")  units: String = "imperial",
        @Query("appid")  apiKey: String = appKey
    ) : WeatherResponse

    @GET("weather")
    suspend fun fetchWeatherLatLng(
        @Query("lat")  latitude: Double,
        @Query("lon")  longitude: Double,
        @Query("units")  units: String = "imperial",
        @Query("appid")  apiKey: String = appKey
    ) : WeatherResponse

    @GET("forecast")
    suspend fun fetchForecastCity(
        @Query("q") cityLocation : String,
//        @Query("cnt")  cnt: String = "5",
        @Query("units")  units: String = "imperial",
        @Query("appid")  apiKey: String = appKey
    ) : ForecastResponse

    @GET("forecast")
    suspend fun fetchForecastLatLng(
        @Query("lat")  latitude: Double,
        @Query("lon")  longitude: Double,
        @Query("units")  units: String = "imperial",
//        @Query("cnt")  cnt: String = "5",
        @Query("appid")  apiKey: String = appKey
    ) : ForecastResponse
}
