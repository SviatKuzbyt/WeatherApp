package com.sviatkuzbyt.weatherapp.weatherfoeweek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sviatkuzbyt.weatherapp.R
import com.sviatkuzbyt.weatherapp.WeatherOf5DayList
import com.sviatkuzbyt.weatherapp.WeatherOfDayList
import com.sviatkuzbyt.weatherapp.setWeatherImage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.util.*

@DelicateCoroutinesApi
class WeatherFor5Days : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_for5_days)

        val city = intent.extras?.getString("city", "kyiv")
        val listDaysOfWeek = arrayOf("Неділя", "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота")
        val listWeatherFor5daysGoal = mutableListOf<WeatherOf5DayList>()
        val listWeatherFor5daysMain = mutableListOf<WeatherOfDayList>()
        val weather5DaysList = findViewById<RecyclerView>(R.id.weather5DaysList)
        val context = this
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener { finish() }

        GlobalScope.launch(Dispatchers.IO){
            try {
                val link = URL("https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=504b4cc387a63ed21f767700acc62351&lang=ua&units=metric")
//
                val json = JSONObject(link.readText()).getJSONArray("list")

                Log.i("info", "отримали інфу")

                var startTime = "${json.getJSONObject(0).getString("dt_txt")[11]}${json.getJSONObject(0).getString("dt_txt")[12]}".toInt()
                var startI = 0
                var day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

                if (startTime == 0|| startTime == 6|| startTime == 12|| startTime == 18){
                    startI++
                    startTime += 3
                }

                Log.i("info", "дізнались час")

                var temp = false

                for (i in startI until json.length() step 2){



                    listWeatherFor5daysMain.add(
                        WeatherOfDayList(
                            "${json.getJSONObject(i).getString("dt_txt").subSequence(11, 13)}:00",
                            setWeatherImage(json.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")),
                            json.getJSONObject(i).getJSONObject("main").getInt("temp"),
                            json.getJSONObject(i).getJSONObject("wind").getString("speed"),
                        )
                    )


                    if (startTime == 21){

                        val date: String = json.getJSONObject(i).getString("dt_txt").subSequence(5, 10) as String


                        listWeatherFor5daysGoal.add(WeatherOf5DayList(listDaysOfWeek[day], date, listWeatherFor5daysMain.toMutableList()))
                        listWeatherFor5daysMain.clear()
                        startTime = 3

                        if (day == 6) day = 0
                        else day ++

                    }else startTime += 6


//                    if (temp){
//                        listWeatherFor5daysGoal.add(WeatherOf5DayList("1", "1", listWeatherFor5daysMain))
//                        listWeatherFor5daysMain.clear()
//                        temp = false
//                    } else startTime +=  6
//
//                    if (startTime == 21) temp = true



                }
//                listWeatherFor5daysGoal.add(WeatherOf5DayList(startTime.toString(), listWeatherFor5daysGoal.size.toString(), listWeatherFor5daysMain))

//                listWeatherFor5daysGoal.add(WeatherOf5DayList("1", "1", listWeatherFor5daysMain))

//                val tempList = mutableListOf<WeatherOfDayList>()
//
//                for (i in 0 until listWeatherFor5daysMain.size){
//                    tempList.add(listWeatherFor5daysMain[i])
//                    startTime += 6
//                    if (startTime == 21){
//                        startTime = 3
//                        listWeatherFor5daysGoal.add(WeatherOf5DayList("1", "1", tempList))
//
//                    }
//                }

                Log.i("info", "Зробили 2 список")
                runOnUiThread{
                    val adapter = FiveDaysListAdapter(listWeatherFor5daysGoal, context)
                    weather5DaysList.adapter = adapter
                    weather5DaysList.layoutManager = LinearLayoutManager(context)
                    weather5DaysList.isNestedScrollingEnabled = false

                    progressBar.visibility = View.GONE
                    weather5DaysList.visibility = View.VISIBLE
                }
            } catch (e: Exception) {}

        }


//
//        val list0 = mutableListOf<WeatherOfDayList>(
//            WeatherOfDayList("1.00", R.drawable.ic_weather_icon_01d, 12, "12"),
//            WeatherOfDayList("2.00", R.drawable.ic_weather_icon_01d, 12, "12"),
//            WeatherOfDayList("3.00", R.drawable.ic_weather_icon_01d, 12, "12"),
//        )
//
//        val list = mutableListOf<WeatherOf5DayList>(
//            WeatherOf5DayList("Понеділок", "12.09", list0)
//        )


    }
}