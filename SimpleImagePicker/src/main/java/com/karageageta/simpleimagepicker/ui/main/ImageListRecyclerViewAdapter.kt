package com.karageageta.simpleimagepicker.ui.main

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


class ImageListRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(parent: ViewGroup, view: View, position: Int, item: SelectableImage) {}
    }

    interface OnItemLongClickListener {
        fun onItemLongClickListener(parent: ViewGroup, view: View, position: Int, item: SelectableImage) = false
    }

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    private lateinit var parent: ViewGroup

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items = ArrayList<SelectableImage>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView

        Glide.with(context)
                .load(File(getItem(position).image.path))
                .into(itemView.image_thumbnail)
        if (getItem(position).isSelected) {
            itemView.view_selected.visibility = View.VISIBLE
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

    fun addAll(items: List<SelectableImage>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun add(item: SelectableImage) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun updateItemView(position: Int, isSelected: Boolean) {
        getItem(position).isSelected = isSelected
        notifyItemChanged(position)
    }

    fun getItem(position: Int): SelectableImage {
        return items[position]
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}