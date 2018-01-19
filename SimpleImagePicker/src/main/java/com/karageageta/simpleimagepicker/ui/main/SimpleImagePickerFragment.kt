package com.karageageta.simpleimagepicker.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.helper.RequestCode
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.karageageta.simpleimagepicker.helper.ExtraName
import com.karageageta.simpleimagepicker.model.data.Album
import com.karageageta.simpleimagepicker.model.data.Config
import com.karageageta.simpleimagepicker.model.data.Image
import com.karageageta.simpleimagepicker.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_simple_image_picker.*
import java.io.Serializable

class SimpleImagePickerFragment : Fragment(),
        SimpleImagePickerContract.View,
        AdapterView.OnItemSelectedListener,
        ImageListRecyclerViewAdapter.OnItemClickListener,
        ImageListRecyclerViewAdapter.OnItemLongClickListener {
    companion object {
        fun newInstance(config: Serializable) = SimpleImagePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ExtraName.CONFIG.name, config)
            }
        }
    }

    private enum class Tag { SPINNER_ALBUM, IMAGE }

    private val config: Config by lazy {
        arguments?.getSerializable(ExtraName.CONFIG.name) as Config
    }

    private lateinit var albumAdapter: ArrayAdapter<String>
    private lateinit var imageAdapter: ImageListRecyclerViewAdapter
    private lateinit var presenter: SimpleImagePickerPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_simple_image_picker, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        albumAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item)
        imageAdapter = ImageListRecyclerViewAdapter(context)
        presenter = SimpleImagePickerPresenter(this, context, config)
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        spinner_album.also {
            it.adapter = albumAdapter
            it.tag = Tag.SPINNER_ALBUM
            it.onItemSelectedListener = this
        }
        recycler_view.also {
            it.layoutManager = GridLayoutManager(this.context, 3)
            it.tag = Tag.IMAGE
            it.adapter = imageAdapter
            it.emptyView = view_empty
        }
        config.noImage?.let { image_empty.setImageDrawable(config.noImage) }

        imageAdapter.also {
            it.onItemClickListener = this
            it.onItemLongClickListener = this
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_simple_image_picker, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.finish -> {
                presenter.saveSelected(imageAdapter.getSelectedImages())
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

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    // ImageListRecyclerViewAdapter.OnItemLongClickListener

    override fun onItemClick(parent: ViewGroup, view: View, position: Int, item: Image, selectable: Boolean) {
        when (parent.tag) {
            Tag.IMAGE -> {
                if (!selectable) return
                imageAdapter.updateItemView(position, config.maxCount)
            }
        }
    }

    // ImageListRecyclerViewAdapter.OnItemClickListener

    override fun onItemLongClickListener(parent: ViewGroup, view: View, position: Int, item: Image, selectable: Boolean): Boolean {
        when (parent.tag) {
            Tag.IMAGE -> {
                if (!selectable) return false
                Intent(context, DetailActivity::class.java)
                        .apply { putExtra(ExtraName.IMAGE_PATH.name, item.path) }
                        .let { startActivity(it) }
                return true
            }
        }
        return false
    }

    // SimpleImagePickerContract.View

    override fun scrollToTop() {
        recycler_view.smoothScrollToPosition(0)
    }

    override fun clearAlbums() {
        albumAdapter.clear()
    }

    override fun addAlbums(items: List<Album>) {
        albumAdapter.addAll(items.map { it.folderName })
        albumAdapter.notifyDataSetChanged()
    }

    override fun clearImages() {
        imageAdapter.clear()
    }

    override fun addImages(items: List<Image>) {
        imageAdapter.addAll(items)
    }

    override fun showImages() {
        view_permission_denied.visibility = View.GONE
    }

    override fun showPermissionDenied() {
        view_permission_denied.visibility = View.VISIBLE

        config.noPermission?.let { image_permission_denied.setImageDrawable(config.noPermission) }
        config.noPermissionText?.let { image_permission_denied.setImageDrawable(config.noPermission) }
    }

    override fun requestPermissions() {
        activity?.let {
            if (ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.PICK_IMAGE.rawValue)
            }
        }
    }

    override fun finishPickImages(items: List<Image>) {
        Intent()
                .apply { putExtra(ExtraName.PICKED_IMAGE.name, items.map { it.path }.toTypedArray()) }
                .let { activity?.setResult(RESULT_OK, it) }
    }

    override fun finish() {
        activity?.finish()
    }
}
