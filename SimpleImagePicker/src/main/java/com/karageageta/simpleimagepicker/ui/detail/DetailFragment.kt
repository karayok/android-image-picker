package com.karageageta.simpleimagepicker.ui.detail

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.helper.ExtraName
import kotlinx.android.synthetic.main.fragment_detail.*
import java.io.File

class DetailFragment : Fragment() {
    companion object {
        fun newInstance(path: String) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ExtraName.IMAGE_PATH.name, path)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments?.getString(ExtraName.IMAGE_PATH.name)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Glide.with(context)
                .load(File(path))
                .into(image_detail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
