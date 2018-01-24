package com.karageageta.sample

import android.app.Application
import com.karageageta.sample.di.component.AppComponent
import com.karageageta.sample.di.module.AppModule
import com.karageageta.sample.di.DaggerAppComponent

class SampleApplication : Application() {
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}