package com.karageageta.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karageageta.simpleimagepicker.SimpleImagePicker
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), View.OnClickListener {
    private enum class Tag { PICK_IMAGE }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_pick_image.tag = Tag.PICK_IMAGE.name
        button_pick_image.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.tag) {
            Tag.PICK_IMAGE.name -> {
                activity?.let {
                    SimpleImagePicker
                            .Builder(it)
                            .pickerAllItemTitle("全部")
                            .start()
                }
            }
        }
    }
}
