package io.github.karageageta.imagepicker.ui.picker

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.support.v4.app.Fragment

import io.github.karageageta.imagepicker.helper.RequestCode
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.github.karageageta.imagepicker.R
import io.github.karageageta.imagepicker.helper.ExtraName
import io.github.karageageta.imagepicker.model.data.Album
import io.github.karageageta.imagepicker.model.data.Config
import io.github.karageageta.imagepicker.model.data.Image
import io.github.karageageta.imagepicker.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_imagepicker_picker.*
import java.io.Serializable

class ImagePickerFragment : Fragment(),
        ImagePickerContract.View,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        ImageListRecyclerViewAdapter.OnItemClickListener,
        ImageListRecyclerViewAdapter.OnItemLongClickListener {
    companion object {
        fun newInstance(config: Serializable) = ImagePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ExtraName.CONFIG.name, config)
            }
        }
    }

    private enum class Tag { SPINNER_ALBUM, IMAGE, BUTTON_SETTING }

    private val config: Config by lazy {
        arguments?.getSerializable(ExtraName.CONFIG.name) as Config
    }

    private lateinit var albumAdapter: ArrayAdapter<String>
    private lateinit var imageAdapter: ImageListRecyclerViewAdapter
    private lateinit var presenter: ImagePickerPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_imagepicker_picker, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        albumAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item)
        imageAdapter = ImageListRecyclerViewAdapter(context)
        presenter = ImagePickerPresenter(this, context, config)
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

        button_setting.also {
            it.visibility = if (config.packageName.isNullOrEmpty()) View.GONE else View.VISIBLE
            it.tag = Tag.BUTTON_SETTING
            it.setOnClickListener(this)
        }

        val permission = context?.let { ContextCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE) }
        if (permission != PERMISSION_GRANTED) {
            activity?.let {
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.PICK_IMAGE.rawValue)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_imagepicker_image_picker, menu)
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

    // ImagePickerContract.View

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

    override fun showPermissionDenied() {
        view_permission_denied.visibility = View.VISIBLE

        config.noPermission?.let { image_permission_denied.setImageDrawable(config.noPermission) }
        config.noPermissionText?.let { image_permission_denied.setImageDrawable(config.noPermission) }
    }

    override fun hidePermissionDenied() {
        view_permission_denied.visibility = View.GONE
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

    override fun onClick(view: View?) {
        when (view?.tag) {
            Tag.BUTTON_SETTING -> {
                config.packageName
                        ?.takeIf { it.isNotEmpty() }
                        ?.let { name ->
                            Intent().apply {
                                action = ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.parse("package:" + name)
                            }.let { activity?.startActivity(it) }
                        }
            }
        }
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
}
