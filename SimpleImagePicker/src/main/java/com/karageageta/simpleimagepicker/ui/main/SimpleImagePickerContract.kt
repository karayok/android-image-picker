package com.karageageta.simpleimagepicker.ui.main

import com.karageageta.simpleimagepicker.model.data.Album
import com.karageageta.simpleimagepicker.model.data.Image
import com.karageageta.simpleimagepicker.ui.BaseContract

interface SimpleImagePickerContract {
    interface View : BaseContract.View {
        fun scrollToTop()
        fun clearAlbums()
        fun addAlbums(items: List<Album>)
        fun clearImages()
        fun addImages(items: List<Image>)
        fun showImages()
        fun showPermissionDenied()
        fun requestPermissions()
        fun finishPickImages(items: List<Image>)
        fun finish()
    }

    interface Presenter<out T : BaseContract.View> : BaseContract.Presenter<T> {
        fun resume()
        fun albums(): List<Album>
        fun albumSelected(position: Int)
        fun saveSelected(items: List<Image>)
    }
}
