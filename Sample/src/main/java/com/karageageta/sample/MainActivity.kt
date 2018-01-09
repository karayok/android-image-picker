package com.karageageta.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var fragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            fragment = MainFragment()
            supportFragmentManager.beginTransaction().add(R.id.content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.content) as MainFragment
        }
    }
}
