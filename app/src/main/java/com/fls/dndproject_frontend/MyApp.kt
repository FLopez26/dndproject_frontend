package com.fls.dndproject_frontend

import android.app.Application
import com.fls.dndproject_frontend.di.appModule
import com.fls.dndproject_frontend.di.retrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                retrofitModule,
                appModule
            )
        }
    }
}