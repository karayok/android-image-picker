package com.karageageta.sample.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.karageageta.sample.R
import com.karageageta.sample.helper.RecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

class MainImageRecyclerViewAdapter(
        private val context: Context
) : RecyclerViewAdapter<File>(context) {
    override fun onCreateView(
            parent: ViewGroup,
            viewType: Int
    ): RecyclerView.ViewHolder = ViewHolder(inflater.inflate(R.layout.item_image, parent, false))

    override fun onBindView(parent: ViewGroup, itemView: View, position: Int) {
        val item = getItem(position)

        Glide.with(context)
                .load(item)
                .into(itemView.image)
        itemView.text_path.text = item.path
    }

    override fun getItemCount(): Int = items.size
}
