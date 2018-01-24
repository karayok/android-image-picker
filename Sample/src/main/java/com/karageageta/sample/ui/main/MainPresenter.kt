package com.karageageta.sample.ui.main

import android.content.Context
import android.widget.Toast

class MainPresenter(
        private val view: MainContract.View?,
        private val context: Context?
) : MainContract.Presenter {
    override fun imagesSelected(paths: List<String>) {
        Toast.makeText(context, paths.joinToString { it }, Toast.LENGTH_SHORT).show()
    }
}