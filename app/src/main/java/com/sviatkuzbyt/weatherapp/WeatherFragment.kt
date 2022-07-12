package com.sviatkuzbyt.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


@DelicateCoroutinesApi
class WeatherFragment : Fragment() {

    var callbacks: OnFragmentCallbacks? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = activity as OnFragmentCallbacks
    }
    interface OnFragmentCallbacks{ fun replaceViews(background: Int ) }
    companion object{ fun newInstance() = WeatherFragment() }

    lateinit var cityName: TextView
    lateinit var imageWeatherMain: ImageView
    lateinit var tempMain: TextView
    lateinit var descriptionMain: TextView
    lateinit var wind: TextView
    lateinit var pressure: TextView
    lateinit var moisture: TextView
    lateinit var precipitation: TextView
    lateinit var weatherTodayList: RecyclerView
    lateinit var weather5DaysBtn: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_veather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityName = view.findViewById(R.id.cityName)
        imageWeatherMain = view.findViewById(R.id.imageWeatherMain)
        tempMain = view.findViewById(R.id.tempMain)
        descriptionMain = view.findViewById(R.id.descriptionMain)
        wind = view.findViewById(R.id.wind)
        pressure = view.findViewById(R.id.pressure)
        moisture = view.findViewById(R.id.moisture)
        precipitation = view.findViewById(R.id.precipitation)
        weatherTodayList = view.findViewById(R.id.weatherTodayList)

        updateWeatherInfo("Самбір")

        weather5DaysBtn = view.findViewById(R.id.weather5Days)
        weather5DaysBtn.setOnClickListener {
//            val intent = Intent(context, WeatherFor5Days::class.java)
//            val bundle: Bundle? = intent.extras
//            bundle?.putString("city", "Sambir")
//            startActivity(intent)

            val intent = Intent(context, WeatherFor5Days::class.java)
            val bundle = Bundle()
            bundle.putString("city", "sambir")
            intent.putExtras(bundle)
            startActivity(intent)
        }

    }


    @SuppressLint("SetTextI18n")
    private fun updateWeatherInfo(city: String){
        var json: JSONObject
        var jsonDay: JSONArray
        val weatherOfDay = mutableListOf<WeatherOfDayList>()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                var url = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&appid=504b4cc387a63ed21f767700acc62351&units=metric&lang=ua")
                json = JSONObject(url.readText())

                url = URL("https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=504b4cc387a63ed21f767700acc62351&cnt=8&lang=ua&units=metric")
                jsonDay = JSONObject(url.readText()).getJSONArray("list")

                for (i in 0 until jsonDay.length()){

                    weatherOfDay.add(
                        WeatherOfDayList(
                            "${jsonDay.getJSONObject(i).getString("dt_txt")[11]}${jsonDay.getJSONObject(i).getString("dt_txt")[12]}:00",
                            setWeatherImage(jsonDay.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")),
                            jsonDay.getJSONObject(i).getJSONObject("main").getInt("temp"),
                            jsonDay.getJSONObject(i).getJSONObject("wind").getString("speed"),
                        )
                    )
                }

                val adapter = DayListAdapter(weatherOfDay, false)


                activity?.runOnUiThread{
                    cityName.text = city
                    tempMain.text = json.getJSONObject("main").getInt("temp").toString() + "°C"
                    descriptionMain.text = json.getJSONArray("weather").getJSONObject(0).getString("description")
                    wind.text = json.getJSONObject("wind").getString("speed") + " м/с"
                    pressure.text = json.getJSONObject("main").getString("pressure") + " гПа"
                    moisture.text = json.getJSONObject("main").getString("humidity") + "%"
                    precipitation.text = "${jsonDay.getJSONObject(0).getDouble("pop").toInt() * 100}%"

                    val weatherImg = json.getJSONArray("weather").getJSONObject(0).getString("icon")
                    imageWeatherMain.setImageResource(setWeatherImage(weatherImg))

                    weatherTodayList.adapter = adapter
                    weatherTodayList.layoutManager = LinearLayoutManager(context)
                    weatherTodayList.isNestedScrollingEnabled = false

                    if("n" in weatherImg){
                        callbacks?.replaceViews(2)
                    }
                    else if ("01" in weatherImg || "02" in weatherImg || "03" in weatherImg){
                        callbacks?.replaceViews(0)
                    }
                    else callbacks?.replaceViews(1)

                }
            } catch (e: Exception) {Toast.makeText(context, "Мережева помилка", Toast.LENGTH_SHORT).show()}

        }
    }


}