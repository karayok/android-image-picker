package com.karageageta.simpleimagepicker.ui.imagepicker

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.karageageta.simpleimagepicker.R
import com.karageageta.simpleimagepicker.helper.ExtraName
import com.karageageta.simpleimagepicker.helper.RequestCode

class SimpleImagePickerActivity : AppCompatActivity() {
    private lateinit var fragment: SimpleImagePickerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_image_picker)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val config = intent.getSerializableExtra(ExtraName.CONFIG.name)
            fragment = SimpleImagePickerFragment.newInstance(config)
            supportFragmentManager.beginTransaction().add(R.id.simple_image_picker_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.simple_image_picker_content) as SimpleImagePickerFragment
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RequestCode.PICK_IMAGE.rawValue -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    fragment.showImages()
                } else {
                    fragment.showPermissionDenied()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
