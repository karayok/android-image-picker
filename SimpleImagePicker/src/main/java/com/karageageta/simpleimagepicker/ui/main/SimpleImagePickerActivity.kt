package com.karageageta.simpleimagepicker.ui.main

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.helper.ExtraName
import com.karageageta.simpleimagepicker.helper.RequestCode

class SimpleImagePickerActivity : AppCompatActivity() {
    private lateinit var fragment: SimpleImagePickerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_image_picker)

        if (savedInstanceState == null) {
            val config = intent.getSerializableExtra(ExtraName.CONFIG.name)
            fragment = SimpleImagePickerFragment.newInstance(config)
            supportFragmentManager.beginTransaction().add(R.id.simple_image_picker_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.simple_image_picker_content) as SimpleImagePickerFragment
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RequestCode.PICK_IMAGE.rawValue) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                fragment.showImages()
                return
            }
            fragment.showPermissionDenied()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
