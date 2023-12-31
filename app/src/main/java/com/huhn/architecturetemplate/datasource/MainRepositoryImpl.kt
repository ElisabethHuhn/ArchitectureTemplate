package com.huhn.architecturetemplate.datasource

import androidx.room.Room
import com.huhn.architecturetemplate.application.ArchitectureTemplateApplication
import com.huhn.architecturetemplate.datasource.localdatasource.AppDatabase
import com.huhn.architecturetemplate.datasource.localdatasource.DBWeatherDao
import com.huhn.architecturetemplate.datasource.remotedatasource.WeatherApiService
import com.huhn.architecturetemplate.datasource.remotedatasource.networkModel.WeatherResponse
import com.huhn.architecturetemplate.ui.WeatherUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

interface MainRepository {
    suspend fun getWeather(
        isByLoc: Boolean,
        latitude: String?,
        longitude: String?,
        city: String?,
        usState: String?,
        country: String?,
    ) : WeatherUIState?
}


class MainRepositoryImpl(
    //remote data source variables
    private val weatherApi: WeatherApiService
) : MainRepository  {

    //local data source variables
    private var db: AppDatabase
    private var dbWeatherDao: DBWeatherDao


    init {
        /*
         * Create the DAOs corresponding to the db tables
         */
        db = createDb()
        dbWeatherDao = db.dbWeatherDao()

        /* ******** THIS IS NOW DONE BY KOIN ******
         * Use RetrofitHelper to create the instance of Retrofit
         * Then use this instance to create an instance of the API
         */
//        weatherApi = RetrofitHelper.getInstance().create(WeatherApiService::class.java)
    }

    suspend fun getDisplayWeather(
        isByLoc: Boolean = false,
        weatherState: WeatherUIState
    ) : WeatherUIState? {
        return getWeather(
            isByLoc = isByLoc,
            latitude = weatherState.latitude,
            longitude = weatherState.longitude,
            city = weatherState.city,
            usState = weatherState.usState,
            country = weatherState.country,
        )
    }

    override suspend fun getWeather(
        isByLoc: Boolean,
        latitude: String?,
        longitude: String?,
        city: String?,
        usState: String?,
        country: String?,
    ) : WeatherUIState? {

        var lat = 0.0
        try {
            lat = latitude?.toDouble() ?: 0.0
        } catch (_: Exception) { }

        var lng  = 0.0
        try {
            lng = longitude?.toDouble() ?: 0.0
        } catch (_: Exception) { }

        var weatherUIState : WeatherUIState? = null

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val deferredWeatherState =coroutineScope.async {
            if (city.isNullOrEmpty())
            {
                weatherUIState =  getWeatherLocal()
            }

            if ((weatherUIState == null ) ||

                ( (weatherUIState?.city.isNullOrEmpty()) &&
                        ((lat != 0.0) && (lng != 0.0)) )           //  ||

//                ( (!weatherUIState?.city.isNullOrEmpty()) &&
//                  (weatherUIState?.city != weatherState.value.city)
//                )
            ){
                weatherUIState = getWeatherRemote(
                    isByLoc = isByLoc,
                    latitude = lat,
                    longitude = lng,
                    city = city,
                    usState = usState,
                    country = country,
                )
            }
            weatherUIState
        }
        return deferredWeatherState.await()
    }



    private suspend fun getWeatherLocal() : WeatherUIState? {
        val weather = dbWeatherDao.findWeatherById(weatherId = 1)

        //convert DB Weather into WeatherUIState
        return weather?.convertToState()
    }

    private suspend fun getWeatherRemote(
        isByLoc: Boolean,
        latitude: Double?,
        longitude: Double?,
        city: String?,
        usState: String?,
        country: String?,
    ) : WeatherUIState? {
        val scope = CoroutineScope(Dispatchers.IO)
        val deferredWeatherResponse = scope.async {
            val weatherResponse : WeatherResponse? =
                when {
                    (isByLoc &&
                     (latitude  != null) && (latitude  != 0.0) &&
                     (longitude != null) && (longitude != 0.0)) -> {
                        weatherApi.fetchWeatherLatLng(
                            latitude = latitude,
                            longitude = longitude
                        )
                    }

                    (!isByLoc && !city.isNullOrEmpty()) -> {
                        var cityString = city
                        if (!usState.isNullOrEmpty()) cityString = "$cityString,$usState"
                        if (!country.isNullOrEmpty()) cityString = "$cityString,$country"
                        weatherApi.fetchWeatherCity(cityLocation = cityString)
                    }

                    else -> null
                }

            weatherResponse?.let {
                //insert the weather into the local DB
                val dbWeather = it.convertToDB()
//                onWeatherChanged(dbWeather.convertToState())
                dbWeatherDao.insertWeather(weather = dbWeather)
            }

            weatherResponse
        }
        return deferredWeatherResponse.await()?.convertToState()
    }


    private fun createDb() : AppDatabase {
        val appContext = ArchitectureTemplateApplication.appContext

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "weather-database"
        ).build()
    }
}


