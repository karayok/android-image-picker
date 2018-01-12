package com.karageageta.simpleimagepicker.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.karageageta.simpleimagepicker.R
import android.content.pm.PackageManager
import com.karageageta.simpleimagepicker.helper.RequestCode
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import android.widget.ArrayAdapter
import com.karageageta.simpleimagepicker.helper.ExtraName
import com.karageageta.simpleimagepicker.helper.Key
import com.karageageta.simpleimagepicker.model.data.Config
import com.karageageta.simpleimagepicker.model.data.Image
import com.karageageta.simpleimagepicker.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_simple_image_picker.*

class SimpleImagePickerFragment : Fragment(),
        SimpleImagePickerContract.View,
        AdapterView.OnItemSelectedListener,
        ImageListRecyclerViewAdapter.OnItemClickListener,
        ImageListRecyclerViewAdapter.OnItemLongClickListener {
    companion object {
        fun newInstance(config: Bundle) = SimpleImagePickerFragment().apply {
            arguments = Bundle().apply {
                putBundle(ExtraName.CONFIG.name, config)
            }
        }
    }

    private enum class Tag { SPINNER_ALBUM, IMAGE }

    private val config = Config()

    private lateinit var imageAdapter: ImageListRecyclerViewAdapter
    private lateinit var presenter: SimpleImagePickerPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_simple_image_picker, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        imageAdapter = ImageListRecyclerViewAdapter(context)
        presenter = SimpleImagePickerPresenter(this, context)
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConfig()

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO : Fix
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(context, R.string.text_permission_denied_external_storage, Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.PICK_IMAGE.rawValue)
            }
            return
        }

        presenter.loadAlbums(config.pickerAllItemTitle)
        spinner_album.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, presenter.albums().map { it.folderName })
        spinner_album.tag = Tag.SPINNER_ALBUM
        spinner_album.onItemSelectedListener = this

        recycler_view.layoutManager = GridLayoutManager(context, 3)
        recycler_view.tag = Tag.IMAGE
        imageAdapter.onItemClickListener = this
        imageAdapter.onItemLongClickListener = this
        recycler_view.adapter = imageAdapter
        recycler_view.emptyView = empty
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RequestCode.PICK_IMAGE.rawValue) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.loadAlbums(config.pickerAllItemTitle)
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_simple_image_picker, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.finish -> {
                presenter.saveSelected(imageAdapter.selectedImages())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // AdapterView.OnItemSelectedListener

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (adapterView?.tag) {
            Tag.SPINNER_ALBUM -> presenter.albumSelected(position)
        }
    }

    // ImageListRecyclerViewAdapter.OnItemClickListener

    override fun onItemLongClickListener(parent: ViewGroup, view: View, position: Int, item: Image): Boolean {
        when (parent.tag) {
            Tag.IMAGE -> {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(ExtraName.IMAGE_PATH.name, item.path)
                startActivity(intent)
                return true
            }
        }
        return false
    }

    // ImageListRecyclerViewAdapter.OnItemLongClickListener

    override fun onItemClick(parent: ViewGroup, view: View, position: Int, item: Image) {
        when (parent.tag) {
            Tag.IMAGE -> {
                imageAdapter.updateItemView(position)
            }
        }
    }

    // SimpleImagePickerContract.View

    override fun scrollToTop() {
        recycler_view.smoothScrollToPosition(0)
    }

    override fun clearImages() {
        imageAdapter.clear()
    }

    override fun addImages(items: List<Image>) {
        imageAdapter.addAll(items)
    }

    override fun finishPickImages(items: List<Image>) {
        val intent = Intent()
        intent.putExtra(ExtraName.PICKED_IMAGE.name, items.map { it.path }.toTypedArray())
        activity?.setResult(RESULT_OK, intent)
    }

    override fun finish() {
        activity?.finish()
    }

    // private

    private fun initConfig() {
        val args = arguments?.getBundle(ExtraName.CONFIG.name)
        args?.getString(Key.PICKER_ALL_ITEM_NAME.name)?.let { config.pickerAllItemTitle = it }
        args?.getInt(Key.MIN_COUNT.name)?.let { config.minCount = it }
        args?.getInt(Key.MAX_COUNT.name)?.let { config.maxCount = it }
    }
}
