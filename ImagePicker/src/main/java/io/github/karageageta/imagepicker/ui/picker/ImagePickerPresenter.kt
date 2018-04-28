package io.github.karageageta.imagepicker.ui.picker

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import io.github.karageageta.imagepicker.R
import io.github.karageageta.imagepicker.model.data.Album
import io.github.karageageta.imagepicker.model.data.Config
import io.github.karageageta.imagepicker.model.data.Image
import java.io.File

class ImagePickerPresenter(
        override val view: ImagePickerContract.View?,
        private val context: Context?,
        private val config: Config
) : ImagePickerContract.Presenter<ImagePickerContract.View> {
    private val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    private var albums = LinkedHashMap<String, Album>()

    override fun resume() {
        when (context?.let { ContextCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE) }) {
            PERMISSION_GRANTED -> {
                view?.hidePermissionDenied()
                load()
            }
            else -> view?.showPermissionDenied()
        }
    }

    override fun albums(): List<Album> {
        return albums.values.toList()
    }

    override fun albumSelected(position: Int) {
        view?.clearImages()
        view?.scrollToTop()
        view?.addImages(albums.values.toList()[position].images)
    }

    override fun saveSelected(items: List<Image>) {
        if (items.isNotEmpty()) {
            view?.finishPickImages(items)
        }
        view?.finish()
    }

    // private

    private fun load() {
        if (albums.isNotEmpty()) {
            return
        }
        loadAlbums()
        view?.clearAlbums()
        view?.addAlbums(albums.values.toList())
        view?.clearImages()
        view?.addImages(albums.values.toList()[0].images)
    }

    private fun loadAlbums() {
        val cursor = context?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED
        )

        context?.getString(R.string.text_imagepicker_album_all_key)?.let { albums.put(it, Album(config.pickerAllItemTitle)) }

        cursor?.takeIf { it.count > 0 }?.use {
            it.moveToLast()
            do {
                val id = it.getLong(it.getColumnIndex(projection[0]))
                val name = it.getString(it.getColumnIndex(projection[1]))
                val path = it.getString(it.getColumnIndex(projection[2]))
                val bucket = it.getString(it.getColumnIndex(projection[3]))

                val file = File(path)
                if (file.exists()) {
                    if (albums[bucket] == null) {
                        albums[bucket] = Album(bucket)
                    }
                    albums[context?.getString(R.string.text_imagepicker_album_all_key)]?.images?.add(Image(id, name, path))
                    albums[bucket]?.images?.add(Image(id, name, path))
                }
            } while (it.moveToPrevious())
        }
    }
}
