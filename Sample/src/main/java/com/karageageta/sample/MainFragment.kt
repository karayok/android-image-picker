package com.karageageta.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karageageta.simpleimagepicker.SimpleImagePicker
import kotlinx.android.synthetic.main.fragment_main.*
import com.bumptech.glide.Glide
import com.karageageta.sample.helper.ExtraName
import java.io.File

class MainFragment : Fragment(), View.OnClickListener {
    companion object {
        fun newInstance(path: Array<String>? = null) = MainFragment().apply {
            arguments = Bundle().apply {
                putStringArray(ExtraName.IMAGES.name, path)
            }
        }
    }

    private enum class Tag { CHOOSE_IMAGE }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_pick_image.tag = Tag.CHOOSE_IMAGE
        button_pick_image.setOnClickListener(this)

        arguments?.getStringArray(ExtraName.IMAGES.name)?.let {
            image.visibility = View.VISIBLE
            Glide.with(context)
                    .load(File(it[0]))
                    .into(image)
        }
    }

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
