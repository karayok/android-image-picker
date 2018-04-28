package io.github.karageageta.imagepicker.ui.picker

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import io.github.karageageta.imagepicker.R
import io.github.karageageta.imagepicker.helper.ExtraName
import io.github.karageageta.imagepicker.helper.RequestCode

class ImagePickerActivity : AppCompatActivity() {
    private lateinit var fragment: ImagePickerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagepicker_picker)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val config = intent.getSerializableExtra(ExtraName.CONFIG.name)
            fragment = ImagePickerFragment.newInstance(config)
            supportFragmentManager.beginTransaction().add(R.id.image_picker_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.image_picker_content) as ImagePickerFragment
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
                    fragment.hidePermissionDenied()
                } else {
                    fragment.showPermissionDenied()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
