package com.huhn.architecturetemplate.datasource.localdatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huhn.architecturetemplate.datasource.localdatasource.dbModel.DBWeather

@Dao
interface DBWeatherDao {
//    @Query("SELECT * FROM DBWeather")
//    suspend fun getWeather(): List<DBWeather>

    @Query("SELECT * FROM DBWeather WHERE id LIKE :weatherId LIMIT 1")
    suspend fun findWeatherById(weatherId : Int): DBWeather?

//    @Query("SELECT * FROM DBWeather WHERE city LIKE :city  LIMIT 1")
//    suspend fun findWeatherByName(city: String): DBWeather
//
//    @Query("SELECT * FROM DBWeather ORDER BY city")
//    suspend fun sortWeatherByName(): List<DBWeather>
//
//    @Query("SELECT * FROM DBWeather ORDER BY id ASC")
//    suspend fun sortWeatherByAscId(): List<DBWeather>
//
//    @Query("SELECT * FROM DBWeather ORDER BY id DESC")
//    suspend fun sortWeatherByDescId(): List<DBWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: DBWeather)

//    @Update
//    suspend fun updateWeather(weather: DBWeather)
//
//    @Delete
//    suspend fun deleteWeather(dbWeather: DBWeather)
}