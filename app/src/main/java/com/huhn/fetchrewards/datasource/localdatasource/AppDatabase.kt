package com.huhn.fetchrewards.datasource.localdatasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huhn.fetchrewards.datasource.localdatasource.dbModel.DBUser

@Database(entities = [DBUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbUserDao(): DBUserDao
}