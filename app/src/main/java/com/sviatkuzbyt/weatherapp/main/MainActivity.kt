package com.sviatkuzbyt.weatherapp.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sviatkuzbyt.weatherapp.*
import com.sviatkuzbyt.weatherapp.DataBase
import com.sviatkuzbyt.weatherapp.cities.CitiesActivity

//WeatherFragment.OnFragmentCallbacks

//var changeBaseDate = false
var changeDB = false
class MainActivity : AppCompatActivity(), WeatherFragment.OnFragmentCallbacks {
    lateinit var mainLayout: RelativeLayout
    lateinit var citiesPager: ViewPager2
    lateinit var geoBtn: Button
    lateinit var textInfo: TextView
    lateinit var btnUpdate: Button
    lateinit var moreBtn: Button
    var currentBackground = 0
    private val cities = mutableListOf<CitiesListConstructor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportActionBar?.setDisplayShowTitleEnabled(false)
        mainLayout = findViewById(R.id.mainLayout)
        citiesPager = findViewById(R.id.citiesPager)
        textInfo = findViewById(R.id.textInfo)
        btnUpdate = findViewById(R.id.btn_update)

//        if (isNetworkAvailable(this)) updateCities()
//        else{
//            citiesPager.visibility = View.GONE
//            textInfo.visibility = View.VISIBLE
//            textInfo.text = getString(R.string.no_internet)
//            btnUpdate.visibility = View.VISIBLE
//            geoBtn.visibility = View.GONE
//        }

        geoBtn = findViewById(R.id.geoBtn)
        geoBtn.setOnClickListener { startActivity(Intent(this, CitiesActivity::class.java)) }

        chekInternetConnection()
        btnUpdate.setOnClickListener { chekInternetConnection() }

        moreBtn = findViewById(R.id.moreBtn)
        moreBtn.setOnClickListener { startActivity(Intent(this, MoreActivity::class.java)) }
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

    private class PagerAdapter(fm: FragmentActivity, private val list: MutableList<CitiesListConstructor>): FragmentStateAdapter(fm){
        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(position: Int): Fragment {
            return WeatherFragment(list[position].city, list[position].cityEn)
        }
    }

    override fun onResume() {
        super.onResume()
        if (changeDB ){
            cities.clear()
            updateCities()
            changeDB = false
        }
    }

    private fun updateCities(){
        try {
            if (textInfo.visibility == View.VISIBLE){
                textInfo.visibility = View.GONE
                btnUpdate.visibility = View.GONE
                citiesPager.visibility = View.VISIBLE
                geoBtn.visibility = View.VISIBLE

            }

            val dataBase = DataBase(this)
            val db = dataBase.readableDatabase
            val cursor = db.query(
                "CITIES", arrayOf("CITY", "CITYEN"), null, null, null, null, null
            )
            if (cursor.moveToFirst()) cities.add(CitiesListConstructor(cursor.getString(0), cursor.getString(1)))
            for (i in 1 until cursor.count){
                if (cursor.moveToNext()) cities.add(CitiesListConstructor(cursor.getString(0), cursor.getString(1)))
            }

            db.close()
            cursor.close()

            citiesPager.adapter = PagerAdapter(this, cities)
        } catch (e: Exception) {}
    }
    private fun chekInternetConnection(){
        if (isNetworkAvailable(this)) updateCities()
        else{
            citiesPager.visibility = View.GONE
            textInfo.visibility = View.VISIBLE
            textInfo.text = getString(R.string.no_internet)
            btnUpdate.visibility = View.VISIBLE
            geoBtn.visibility = View.GONE
        }
    }
}

