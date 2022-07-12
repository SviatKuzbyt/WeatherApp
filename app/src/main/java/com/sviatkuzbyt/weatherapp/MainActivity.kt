package com.sviatkuzbyt.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout


class MainActivity : AppCompatActivity(), WeatherFragment.OnFragmentCallbacks {
    lateinit var fragment: androidx.fragment.app.FragmentContainerView
    lateinit var process: ProgressBar
    lateinit var mainLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        fragment = findViewById(R.id.fragmentContainerView)
        process = findViewById(R.id.mainProgressBar)
        mainLayout = findViewById(R.id.mainLayout)
            }

    override fun replaceViews(background: Int) {
        process.visibility = View.GONE
        fragment.visibility = View.VISIBLE
        when(background){
            2 -> mainLayout.background = getDrawable(R.drawable.background_night)
            1 -> mainLayout.background = getDrawable(R.drawable.background_cloudy)
            else -> mainLayout.background = getDrawable(R.drawable.background)
        }
    }
}