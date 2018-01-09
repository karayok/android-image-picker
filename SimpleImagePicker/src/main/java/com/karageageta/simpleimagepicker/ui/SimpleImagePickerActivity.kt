package com.karageageta.simpleimagepicker.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.karageageta.simpleimagepicker.R

class SimpleImagePickerActivity : AppCompatActivity() {
    private lateinit var fragment: SimpleImagePickerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_image_picker)

        if (savedInstanceState == null) {
            fragment = SimpleImagePickerFragment()
            supportFragmentManager.beginTransaction().add(R.id.simple_image_picker_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.simple_image_picker_content) as SimpleImagePickerFragment
        }
    }
}
