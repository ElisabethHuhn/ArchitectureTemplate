package com.huhn.architecturetemplate.datasource

import android.location.Location
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.huhn.architecturetemplate.application.ArchitectureTemplateApplication
import com.huhn.architecturetemplate.datasource.localdatasource.AppDatabase
import com.huhn.architecturetemplate.datasource.localdatasource.DBWeatherDao
import com.huhn.architecturetemplate.datasource.localdatasource.dbModel.DBWeather
import com.huhn.architecturetemplate.datasource.remotedatasource.WeatherApiService
import com.huhn.architecturetemplate.datasource.remotedatasource.networkModel.WeatherResponse
import com.huhn.architecturetemplate.ui.WeatherUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface WeatherRepository {
    fun getWeather(
        isByLoc: Boolean,
        latitude: String?,
        longitude: String?,
        city: String?,
        usState: String?,
        country: String?,
    )
}


class WeatherRepositoryImpl(
    //remote data source variables
    val weatherApi: WeatherApiService
) : WeatherRepository  {

    //local data source variables
    private var db: AppDatabase
    private var dbWeatherDao: DBWeatherDao

    val weatherState = MutableStateFlow(WeatherUIState())

    private val _locationState  = MutableStateFlow( LocationState())
    val locationState = _locationState.asStateFlow()

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

    fun onDisplayWeather(isByLoc: Boolean = false) {
        getWeather(
            isByLoc = isByLoc,
            latitude = weatherState.value.latitude,
            longitude = weatherState.value.longitude,
            city = weatherState.value.city,
            usState = weatherState.value.usState,
            country = weatherState.value.country,
        )
    }

     override fun getWeather(
         isByLoc: Boolean,
         latitude: String?,
         longitude: String?,
         city: String?,
         usState: String?,
         country: String?,
     ) {
         var lat = 0.0
         try {
             lat = latitude?.toDouble() ?: 0.0
         } catch (_: Exception) { }

         var lng  = 0.0
         try {
             lng = longitude?.toDouble() ?: 0.0
         } catch (_: Exception) { }

        //The Compose UI will recompose when the view-model.weatherResponse changes
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            var weather : WeatherUIState? = null

            if (weatherState.value.city.isEmpty())
            {
                weather =  getWeatherLocal()
            }

            if ((weather == null ) ||
                ( (weatherState.value.city.isEmpty()) && ((lat != 0.0) && (lng != 0.0)) ) ||
                ((weatherState.value.city.isNotEmpty()) && (weather.city != weatherState.value.city))

                ){

                getWeatherRemote(
                    isByLoc = isByLoc,
                    latitude = lat,
                    longitude = lng,
                    city = city,
                    usState = usState,
                    country = country,
                )
            } else {
                onWeatherChanged(weather)
            }
        }
    }

    private suspend fun getWeatherLocal() : WeatherUIState? {
        val weather = dbWeatherDao.findWeatherById(weatherId = 1)

        //convert DB Weather into WeatherUIState
        return weather?.let {
            it.convertToState()
        }
    }

    suspend fun saveWeatherLocal(dbWeather: DBWeather) {
        dbWeatherDao.insertWeather(weather = dbWeather)
    }

    private fun getWeatherRemote(
        isByLoc: Boolean,
        latitude: Double?,
        longitude: Double?,
        city: String?,
        usState: String?,
        country: String?,
    ) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val weatherResponse : WeatherResponse? =
                when {
                    (isByLoc &&
                            ((latitude  != null) &&
                                    (longitude != null) &&
                                    ( (latitude != 0.0) && (longitude != 0.0))
                                    )
                            ) -> {
                        weatherApi.fetchWeatherLatLng(
                            latitude = latitude,
                            longitude = longitude
                        )
                    }

                    (!isByLoc && !city.isNullOrEmpty())-> {
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
                onWeatherChanged(dbWeather.convertToState())
                dbWeatherDao.insertWeather(weather = dbWeather)
            }
        }
    }

    private  fun onWeatherChanged(weather: WeatherUIState){
        weatherState.value = weather
    }

    fun onInitializedChanged(data : Boolean){
        weatherState.update { it.copy(isInitialized = data) }
    }
    fun onCityChanged(data: String) {
        weatherState.update { it.copy(city = data) }
    }
    fun onUsStateChanged(data: String) {
        weatherState.update { it.copy(usState = data) }
    }
    fun onCountryChanged(data: String) {
        weatherState.update { it.copy(country = data) }
    }
    fun onLatitudeChanged(data: String) {
        weatherState.update { it.copy(latitude = data) }
    }
    fun onLongitudeChanged(data: String) {
        weatherState.update { it.copy(longitude = data) }
    }
    fun onErrorChanged(data: String) {
        weatherState.update { it.copy(errorMsg = data) }
    }

    fun onLocationChanged(location: Location?) {
        onLocLatitudeChanged(data = location?.latitude.toString() )
        onLocLongitudeChanged(data = location?.longitude.toString() )
    }
    private fun onLocLatitudeChanged(data: String) {
        _locationState.update { it.copy(locLatitude = data) }
    }
    private fun onLocLongitudeChanged(data: String) {
       _locationState.update { it.copy(locLongitude = data) }
    }
    fun onGetLocation() {
        onLatitudeChanged(data = locationState.value.locLatitude)
        onLongitudeChanged(data = locationState.value.locLongitude)
    }

    fun onInitialization() {
        weatherState.update { it.copy(
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

    private fun createDb() : AppDatabase {
        val appContext = ArchitectureTemplateApplication.appContext

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "weather-database"
        ).build()
    }
}

data class LocationState(
    val location: Location? = null,
    val locLatitude : String = "0.0",
    val locLongitude: String = "0.0",
    val fusedClient : FusedLocationProviderClient? = null,
)
