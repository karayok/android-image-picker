package com.karageageta.simpleimagepicker.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.helper.ExtraName

class SimpleImagePickerActivity : AppCompatActivity() {
    private lateinit var fragment: SimpleImagePickerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_image_picker)

        if (savedInstanceState == null) {
            val config = intent.getBundleExtra(ExtraName.CONFIG.name)
            fragment = SimpleImagePickerFragment.newInstance(config)
            supportFragmentManager.beginTransaction().add(R.id.simple_image_picker_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.simple_image_picker_content) as SimpleImagePickerFragment
        }
    }
}
