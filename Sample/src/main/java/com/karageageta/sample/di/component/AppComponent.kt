package com.karageageta.sample.di.component

import com.karageageta.sample.di.module.AppModule
import com.karageageta.sample.di.module.MainPresenterModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    // main
    fun mainComponent(mainPresenterModule: MainPresenterModule): MainComponent
}
