package com.karageageta.sample.ui.main

import android.content.Context
import java.io.File

class MainPresenter(
        private val view: MainContract.View,
        private val context: Context
) : MainContract.Presenter {
    override fun setImages(paths: List<String>) {
        view.addImages(paths.map { File(it) })
    }
}