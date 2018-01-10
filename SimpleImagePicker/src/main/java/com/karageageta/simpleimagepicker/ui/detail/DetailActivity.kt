package com.karageageta.simpleimagepicker.ui.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.helper.ExtraName

import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var fragment: DetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (savedInstanceState == null) {
            val path = intent.getStringExtra(ExtraName.IMAGE_PATH.name)
            fragment = DetailFragment.newInstance(path)
            supportFragmentManager.beginTransaction().add(R.id.detail_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.detail_content) as DetailFragment
        }
    }
}
