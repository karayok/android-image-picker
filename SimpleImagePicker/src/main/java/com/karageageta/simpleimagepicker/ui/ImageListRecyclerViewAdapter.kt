package com.karageageta.simpleimagepicker.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.model.data.SelectableImage
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File


class ImageListRecyclerViewAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(parent: ViewGroup, view: View, position: Int) {}
    }

    interface OnItemLongClickListener {
        fun onItemLongClickListener(parent: ViewGroup, view: View, position: Int) = false
    }

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    private lateinit var parent: ViewGroup

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items = ArrayList<SelectableImage>()
    private val requestManager = Glide.with(context)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        requestManager
                .load(File(getItem(position).image.path))
                .into(holder.itemView.image_thumbnail)

        holder.itemView.setOnClickListener { v -> onItemClickListener?.onItemClick(parent, v, position) }
        holder.itemView.setOnLongClickListener { v -> onItemLongClickListener?.onItemLongClickListener(parent, v, position) == true }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        this.parent = parent!!
        return ViewHolder(inflater.inflate(R.layout.item_image, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Public

    fun getPosition(item: SelectableImage): Int {
        return items.indexOf(item)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addAll(items: List<SelectableImage>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun add(item: SelectableImage) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun updateItemView(position: Int, bookmarked: Boolean) {
        getItem(position).isSelected = bookmarked
        notifyItemChanged(position)
    }

    // private

    private fun getItem(position: Int): SelectableImage {
        return items[position]
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}