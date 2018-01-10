package com.karageageta.simpleimagepicker.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.karageageta.simpleimagepicker.R
import android.content.pm.PackageManager
import com.karageageta.simpleimagepicker.helper.RequestCode
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.widget.AdapterView
import android.widget.Toast
import com.karageageta.simpleimagepicker.model.data.Album
import android.widget.ArrayAdapter
import com.karageageta.simpleimagepicker.helper.ExtraName
import com.karageageta.simpleimagepicker.helper.Key
import com.karageageta.simpleimagepicker.model.data.Config
import com.karageageta.simpleimagepicker.model.data.Image
import com.karageageta.simpleimagepicker.model.data.SelectableImage
import kotlinx.android.synthetic.main.fragment_simple_image_picker.*
import java.io.File


class SimpleImagePickerFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object {
        fun newInstance(config: Bundle) = SimpleImagePickerFragment().apply {
            arguments = Bundle().apply {
                putBundle(ExtraName.CONFIG.name, config)
            }
        }
    }

    private enum class Tag { SPINNER_ALBUM, IMAGE }

    private val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    private lateinit var imageAdapter: ImageListRecyclerViewAdapter
    private val config = Config()
    private var albums = ArrayList<Album>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_simple_image_picker, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        imageAdapter = ImageListRecyclerViewAdapter(context)
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.getBundle(ExtraName.CONFIG.name)
        args?.getString(Key.PICKER_ALL_ITEM_NAME.name)?.let { config.pickerAllItemTitle = it }
        args?.getInt(Key.MIN_COUNT.name)?.let { config.minCount = it }
        args?.getInt(Key.MAX_COUNT.name)?.let { config.maxCount = it }

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO : Fix
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(context, R.string.text_permission_denied_external_storage, Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.PICK_IMAGE.rawValue)
            }
            return
        }

        // TODO : fix for empty
        albums = loadAlbums() as ArrayList<Album>
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, albums.map { it.folderName })
        spinner_album.adapter = adapter
        spinner_album.tag = Tag.SPINNER_ALBUM
        spinner_album.onItemSelectedListener = this

        recycler_view.layoutManager = GridLayoutManager(context, 3)
        recycler_view.tag = Tag.IMAGE
        recycler_view.adapter = imageAdapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RequestCode.PICK_IMAGE.rawValue) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                albums = loadAlbums() as ArrayList<Album>
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // AdapterView.OnItemSelectedListener

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(context, albums[position].folderName, Toast.LENGTH_SHORT).show()
        when (adapterView?.tag) {
            Tag.SPINNER_ALBUM -> {
                imageAdapter.clear()
                val selectableImages = albums[position].images.map { SelectableImage(it) }
                imageAdapter.addAll(selectableImages)
            }
        }
    }


    // private

    private fun loadAlbums(): List<Album> {
        val cursor = context?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED
        )

        val albumMap = LinkedHashMap<String, Album>()
        albumMap.put(getString(R.string.text_album_all_key), Album(config.pickerAllItemTitle))

        if (cursor!!.moveToLast()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(projection[0]))
                val name = cursor.getString(cursor.getColumnIndex(projection[1]))
                val path = cursor.getString(cursor.getColumnIndex(projection[2]))
                val bucket = cursor.getString(cursor.getColumnIndex(projection[3]))

                val file = createValidFile(path)
                if (file != null && file.exists()) {
                    if (albumMap[bucket] == null) {
                        albumMap.put(bucket, Album(bucket))
                    }
                    albumMap[getString(R.string.text_album_all_key)]?.images?.add(Image(id, name, path))
                    albumMap[bucket]?.images?.add(Image(id, name, path))
                }
            } while (cursor.moveToPrevious())
        }
        cursor.close()

        return albumMap.values.toList()
    }

    private fun createValidFile(path: String): File? {
        if (path.isEmpty()) {
            return null
        }
        return try {
            File(path)
        } catch (e: Exception) {
            null
        }
    }
}
