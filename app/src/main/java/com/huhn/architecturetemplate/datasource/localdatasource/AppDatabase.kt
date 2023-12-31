package com.huhn.architecturetemplate.datasource.localdatasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huhn.architecturetemplate.datasource.localdatasource.dbModel.DBWeather

@Database(entities = [DBWeather::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbWeatherDao(): DBWeatherDao
}