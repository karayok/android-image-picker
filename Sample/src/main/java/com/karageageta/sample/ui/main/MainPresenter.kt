package com.karageageta.sample.ui.main

import android.content.Context

class MainPresenter(
        override val view: MainContract.View?,
        private val context: Context?
) : MainContract.Presenter<MainContract.View> {
    override fun imagesSelected(pats: List<String>) {
    }
}