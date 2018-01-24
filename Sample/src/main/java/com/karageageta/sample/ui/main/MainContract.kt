package com.karageageta.sample.ui.main

import com.karageageta.sample.BaseContract

interface MainContract {
    interface View : BaseContract.View {
        fun addImages()
    }

    interface Presenter: BaseContract.Presenter {
        fun imagesSelected(paths: List<String>)
    }
}