package io.github.karageageta.imagepicker.ui.picker

import io.github.karageageta.imagepicker.model.data.Album
import io.github.karageageta.imagepicker.model.data.Image
import io.github.karageageta.imagepicker.ui.BaseContract

interface ImagePickerContract {
    interface View : BaseContract.View {
        fun scrollToTop()
        fun clearAlbums()
        fun addAlbums(items: List<Album>)
        fun clearImages()
        fun addImages(items: List<Image>)
        fun showPermissionDenied()
        fun hidePermissionDenied()
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
