package com.karageageta.sample.ui.main

import com.karageageta.sample.BaseContract

interface MainContract {
    interface View : BaseContract.View {
        fun addImages()
    }

    interface Presenter<out T : BaseContract.View> : BaseContract.Presenter<T> {
        fun imagesSelected(pats: List<String>)
    }
}