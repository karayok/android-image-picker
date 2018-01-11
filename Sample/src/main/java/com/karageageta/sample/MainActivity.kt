package com.karageageta.sample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.karageageta.simpleimagepicker.helper.ExtraName
import com.karageageta.simpleimagepicker.helper.RequestCode

class MainActivity : AppCompatActivity() {
    private lateinit var fragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            fragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.content) as MainFragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.PICK_IMAGE.rawValue && resultCode == RESULT_OK && data != null) {
            val images = data.getStringArrayExtra(ExtraName.PICKED_IMAGE.name)
            fragment = MainFragment.newInstance(images)
            supportFragmentManager.beginTransaction().add(R.id.content, fragment).commit()
        }
    }
}
