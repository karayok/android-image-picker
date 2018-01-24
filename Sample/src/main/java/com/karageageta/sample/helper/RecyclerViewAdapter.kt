package com.karageageta.sample.helper

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class RecyclerViewAdapter<T>(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(parent: ViewGroup, view: View, position: Int) {}
    }

    interface OnItemLongClickListener {
        fun onItemLongClickListener(parent: ViewGroup, view: View, position: Int) = false
    }

    abstract fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract fun onBindView(parent: ViewGroup, itemView: View, position: Int)

    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected val items = ArrayList<T>()
    protected lateinit var parent: ViewGroup
    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.parent = parent
        return onCreateView(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindView(parent, holder.itemView, position)
        holder.itemView
                .setOnClickListener { view ->
                    onItemClickListener
                            ?.onItemClick(parent, view, position)
                }
        holder.itemView
                .setOnLongClickListener { view ->
                    onItemLongClickListener?.onItemLongClickListener(
                            parent,
                            view,
                            position
                    ) == true
                }
    }

    // public

    fun getItem(position: Int): T {
        return items[position]
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun setAll(items: List<T>) {
        clear()
        addAll(items)
    }

    fun addAll(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    // Inner class

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}