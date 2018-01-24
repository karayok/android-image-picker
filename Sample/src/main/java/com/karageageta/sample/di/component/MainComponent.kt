package com.karageageta.sample.di.component

import com.karageageta.sample.di.module.MainPresenterModule
import com.karageageta.sample.ui.main.MainFragment
import dagger.Subcomponent

@Subcomponent(modules = [(MainPresenterModule::class)])
interface MainComponent {
    fun inject(fragment: MainFragment)
}