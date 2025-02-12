package com.huhn.fetchrewards.datasource

import com.huhn.fetchrewards.datasource.remotedatasource.UserApiService
import com.huhn.fetchrewards.datasource.remotedatasource.networkModel.User

interface MainRepository {
    suspend fun getUserList() : ArrayList<User>
}


class MainRepositoryImpl(
    //remote data source variables
    private val userApi: UserApiService
) : MainRepository  {

    //local data source variables
//    private var db: AppDatabase
//    private var dbUserDao: DBUserDao


    init {
        /*
         * Create the DAOs corresponding to the db tables
         */
//        db = createDb()
//        dbUserDao = db.dbUserDao()

        /* ******** THIS IS NOW DONE BY KOIN module ******
         * Use RetrofitHelper to create the instance of Retrofit
         * Then use this instance to create an instance of the API
         */
//        userApi = RetrofitHelper.getInstance().create(WeatherApiService::class.java)
    }

    override suspend fun getUserList() : ArrayList<User> {
        return userApi.fetchUsers()
    }




//    private fun createDb() : AppDatabase {
//        val appContext = FetchRewardsApplication.appContext
//
//        return Room.databaseBuilder(
//            appContext,
//            AppDatabase::class.java, "user-database"
//        ).build()
//    }
}


