package com.karageageta.sample.ui.main

import com.karageageta.sample.BaseContract
import java.io.File

interface MainContract {
    interface View : BaseContract.View {
        fun addImages(items: List<File>)
        fun imagesSelected(paths: List<String>)
    }

    interface Presenter: BaseContract.Presenter {
        fun setImages(paths: List<String>)
    }
}