package com.karageageta.sample.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karageageta.simpleimagepicker.SimpleImagePicker
import kotlinx.android.synthetic.main.fragment_main.*
import com.karageageta.sample.R
import com.karageageta.sample.SampleApplication
import com.karageageta.sample.di.module.MainPresenterModule
import javax.inject.Inject

class MainFragment : Fragment(),
        MainContract.View,
        View.OnClickListener {
    companion object {
        fun newInstance() = MainFragment()
    }

    private enum class Tag { CHOOSE_IMAGE }

    @Inject lateinit var presenter: MainContract.Presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as SampleApplication).component
                .mainComponent(MainPresenterModule(this))
                .inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_pick_image.tag = Tag.CHOOSE_IMAGE
        button_pick_image.setOnClickListener(this)
    }

    // MainContract.View

    override fun addImages() {
    }

    // View.OnClickListener

    override fun onClick(view: View?) {
        when (view?.tag) {
            Tag.CHOOSE_IMAGE -> {
                activity?.let {
                    SimpleImagePicker
                            .Builder(it)
                            .pickerAllItemTitle(getString(R.string.text_all))
                            .start()
                }
            }
        }
    }
}
