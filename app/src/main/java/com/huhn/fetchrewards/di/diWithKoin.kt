package com.huhn.fetchrewards.di


import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.huhn.fetchrewards.datasource.remotedatasource.UserApiService
import com.huhn.fetchrewards.viewmodel.MainViewModelImpl
import com.huhn.fetchrewards.datasource.MainRepositoryImpl


/*
 * Koin needs:
 * o A dependency in the build.gradle(app) file
 * o Include the application class in the manifest
 * o to be started in the Application class
 * o koin module(s) defining how classes are to be created
 */

//const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com"

//Note the use of Constructor DSL rather than the older single<>{  } syntax
val koinModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    singleOf(::MainRepositoryImpl)
    viewModelOf(::MainViewModelImpl)
}