package com.sviatkuzbyt.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

//WeatherFragment.OnFragmentCallbacks
class MainActivity : AppCompatActivity(), WeatherFragment.OnFragmentCallbacks {
    lateinit var mainLayout: RelativeLayout
    lateinit var citiesPager: ViewPager2
    var currentBackground = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cities = arrayOf("Самбір", "Київ", "Львів", "Париж")
//        supportActionBar?.setDisplayShowTitleEnabled(false)
        mainLayout = findViewById(R.id.mainLayout)
        citiesPager = findViewById(R.id.citiesPager)
        citiesPager.adapter = PagerAdapter(this, cities)
    }

    override fun replaceViews(background: Int) {
        if (background == 2 && currentBackground != 2){
            mainLayout.background = getDrawable(R.drawable.background_night)
            currentBackground = 2
        } else if (background == 1 && currentBackground != 1){
            mainLayout.background = getDrawable(R.drawable.background_cloudy)
            currentBackground = 1
        } else if(background == 0 && currentBackground != 0){
            mainLayout.background = getDrawable(R.drawable.background)
            currentBackground = 0
        }
    }

    private class PagerAdapter(fm: FragmentActivity, private val list: Array<String>): FragmentStateAdapter(fm){
        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(position: Int): Fragment {
            return WeatherFragment(list[position])
        }
    }
}