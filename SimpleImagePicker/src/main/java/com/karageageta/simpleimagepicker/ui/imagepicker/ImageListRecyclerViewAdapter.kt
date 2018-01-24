package com.karageageta.simpleimagepicker.ui.imagepicker

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.model.data.Image
import kotlinx.android.synthetic.main.item_image_picker_image.view.*
import java.io.File

class ImageListRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(
                parent: ViewGroup,
                view: View,
                position: Int,
                item: Image,
                selectable: Boolean
        ) {
        }
    }

    interface OnItemLongClickListener {
        fun onItemLongClickListener(
                parent: ViewGroup,
                view: View,
                position: Int,
                item: Image,
                selectable: Boolean
        ) = false
    }

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    private lateinit var parent: ViewGroup
    private val selectedImages = ArrayList<Image>()
    private var selectable: Boolean = true

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items = ArrayList<Image>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        this.parent = parent!!
        return ViewHolder(inflater.inflate(R.layout.item_image_picker_image, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView
        val item = getItem(position)

        itemView.view_disable.visibility = View.GONE
        itemView.view_selected.visibility = View.GONE

        Glide.with(context)
                .load(File(item.path))
                .into(itemView.image_thumbnail)

        var isSelectedImage = false
        if (selectedImages.contains(item)) {
            itemView.apply {
                view_selected.visibility = View.VISIBLE
                text_index.text = (selectedImages.indexOf(item) + 1).toString()
            }
            isSelectedImage = true
        }

        if (!selectable && !isSelectedImage) {
            itemView.view_disable.visibility = View.VISIBLE
        }

        itemView.also {
            it.setOnClickListener { view ->
                onItemClickListener
                        ?.onItemClick(
                                parent,
                                view,
                                position,
                                getItem(position),
                                selectable || isSelectedImage
                        )
            }
            it.setOnLongClickListener { view ->
                onItemLongClickListener
                        ?.onItemLongClickListener(
                                parent,
                                view,
                                position,
                                getItem(position),
                                selectable || isSelectedImage
                        ) == true
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Public

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addAll(items: List<Image>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun add(item: Image) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun updateItemView(position: Int, maxSelectCount: Int) {
        if (selectedImages.contains(getItem(position))) {
            selectedImages.remove(getItem(position))
            selectable = selectedImages.size < maxSelectCount
            notifyDataSetChanged()
            return
        }

        selectedImages.add(getItem(position))
        if (selectedImages.size == maxSelectCount) {
            selectable = false
            notifyDataSetChanged()
            return
        }
        notifyItemChanged(position)
    }

    fun getSelectedImages(): List<Image> {
        return selectedImages
    }

    // private

    private fun getItem(position: Int): Image {
        return items[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
