package com.karageageta.sample.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karageageta.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_main.*
import com.karageageta.sample.R
import com.karageageta.sample.SampleApplication
import com.karageageta.sample.di.module.MainPresenterModule
import java.io.File
import javax.inject.Inject

class MainFragment : Fragment(),
        MainContract.View,
        View.OnClickListener {
    companion object {
        fun newInstance() = MainFragment()
    }

    private enum class Tag { CHOOSE_IMAGE }

    @Inject
    lateinit var presenter: MainContract.Presenter
    lateinit var adapter: MainImageRecyclerViewAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as SampleApplication).component
                .mainComponent(MainPresenterModule(this))
                .inject(this)
        adapter = MainImageRecyclerViewAdapter(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_pick_image.also {
            it.tag = Tag.CHOOSE_IMAGE
            it.setOnClickListener(this)
        }
        view_recycle.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = this.adapter
            view_recycle.emptyView = view_empty
        }
    }

    // MainContract.View

    override fun addImages(items: List<File>) {
        adapter.addAll(items)
    }

    override fun imagesSelected(paths: List<String>) {
        presenter.setImages(paths)
    }

    // View.OnClickListener

    override fun onClick(view: View?) {
        when (view?.tag) {
            Tag.CHOOSE_IMAGE -> {
                activity?.let {
                    ImagePicker
                            .Builder(it)
                            .pickerAllItemTitle(getString(R.string.text_all))
                            .packageName(context?.packageName ?: "")
                            .start()
                }
            }
        }
    }
}
