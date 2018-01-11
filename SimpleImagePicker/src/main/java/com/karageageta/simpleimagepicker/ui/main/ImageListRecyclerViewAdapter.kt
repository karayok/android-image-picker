package com.karageageta.simpleimagepicker.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.model.data.Image
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

class ImageListRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(parent: ViewGroup, view: View, position: Int, item: Image) {}
    }

    interface OnItemLongClickListener {
        fun onItemLongClickListener(parent: ViewGroup, view: View, position: Int, item: Image) = false
    }

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    private lateinit var parent: ViewGroup
    private val selectedImages = ArrayList<Image>()

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items = ArrayList<Image>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView
        val item = getItem(position)

        Glide.with(context)
                .load(File(item.path))
                .into(itemView.image_thumbnail)
        if (selectedImages.contains(item)) {
            itemView.view_selected.visibility = View.VISIBLE
            itemView.text_index.text = (selectedImages.indexOf(item) + 1).toString()
        } else {
            itemView.view_selected.visibility = View.GONE
        }

        itemView.setOnClickListener { v -> onItemClickListener?.onItemClick(parent, v, position, getItem(position)) }
        itemView.setOnLongClickListener { v -> onItemLongClickListener?.onItemLongClickListener(parent, v, position, getItem(position)) == true }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        this.parent = parent!!
        return ViewHolder(inflater.inflate(R.layout.item_image, parent, false))
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

    fun updateItemView(position: Int) {
        if (selectedImages.contains(getItem(position))) {
            unSelectItem(position)
            return
        }
        selectItem(position)
    }

    fun selectedImages(): List<Image> {
        return selectedImages
    }

    // private

    private fun selectItem(position: Int) {
        selectedImages.add(getItem(position))
        notifyItemChanged(position)
    }

    private fun unSelectItem(position: Int) {
        selectedImages.remove(getItem(position))
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): Image {
        return items[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
