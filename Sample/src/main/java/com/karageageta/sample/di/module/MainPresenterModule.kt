package com.karageageta.sample.di.module

import android.content.Context
import com.karageageta.sample.ui.main.MainContract
import com.karageageta.sample.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainPresenterModule(private val view: MainContract.View) {
    @Provides
    internal fun providesMainPresenter(
            context: Context
    ): MainContract.Presenter = MainPresenter(view, context)
}