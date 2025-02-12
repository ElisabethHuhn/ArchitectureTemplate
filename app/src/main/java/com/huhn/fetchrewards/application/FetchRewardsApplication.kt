package com.huhn.fetchrewards.application

import android.app.Application
import android.content.Context
import com.huhn.fetchrewards.di.koinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class FetchRewardsApplication :Application() {
    companion object {
        lateinit  var appContext: Context
    }
    override fun onCreate() {
        super.onCreate()

        appContext = this.applicationContext

        startKoin {
            androidLogger()
            androidContext(this@FetchRewardsApplication)
            modules(listOf(koinModule))
        }
    }
}