package com.sviatkuzbyt.weatherapp.cities

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sviatkuzbyt.weatherapp.*
import com.sviatkuzbyt.weatherapp.main.changeDB
import kotlinx.coroutines.*
import org.json.JSONArray
import java.net.URL

@DelicateCoroutinesApi
class CitiesActivity : AppCompatActivity() {

    lateinit var cursor: Cursor
    lateinit var db: SQLiteDatabase
    lateinit var adapter: CitiesListAdapter
    lateinit var citiesRecyclerView: RecyclerView
    private var list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)
        citiesRecyclerView = findViewById(R.id.citiesRecyclerView)


        val dataBase = DataBase(this)
        try {
            db = dataBase.writableDatabase
            cursor = db.query(
                "CITIES", arrayOf("CITY"), null, null, null, null, null
            )
            if (cursor.moveToFirst()) list.add(cursor.getString(0))
            for (i in 1 until cursor.count){
                if (cursor.moveToNext()) list.add(cursor.getString(0))
            }

        }catch (e: Exception) {Toast.makeText(this, "Помилка з БД", Toast.LENGTH_SHORT).show()}

        adapter = CitiesListAdapter(list, this)
        citiesRecyclerView.adapter = adapter
        citiesRecyclerView.layoutManager = LinearLayoutManager(this)

        val addBtn = findViewById<Button>(R.id.addBtn)
        addBtn.setOnClickListener {
            val bottomSheetDialog = AddCityFragment(this)
            bottomSheetDialog.show(supportFragmentManager, "add city")

        }

        val backCities = findViewById<Button>(R.id.backCities)
        backCities.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor.close()
        db.close()
    }

    fun deleteRecord(city: String){
        try {
            db.delete("CITIES", "CITY = ?", arrayOf(city))
            list.remove(city)
            adapter.notifyDataSetChanged()
            if(!changeDB) changeDB = true

        } catch (e: Exception){Toast.makeText(this, "Помилка видалення", Toast.LENGTH_SHORT).show()}

    }
    //        when (city) {
//            city.isEmpty() -> Toast.makeText(this, "Введіть назву міста", Toast.LENGTH_LONG).show()
//            "Путін" -> Toast.makeText(this, "- хуйло! (фу, з великої...)", Toast.LENGTH_LONG).show()
//            "путін" -> Toast.makeText(this, "- хуйло!", Toast.LENGTH_LONG).show()
//            "Україна" -> Toast.makeText(this, "понад усе!", Toast.LENGTH_LONG).show()
    fun addRecord(city: String? = "None"){

        if (city == null || city.isEmpty()) Toast.makeText(this, "Введіть назву міста", Toast.LENGTH_LONG).show()
        else if (city in list) Toast.makeText(this, "Місто вже додано", Toast.LENGTH_LONG).show()
        else {
            var city2 = "None"
            var city3 = "None"
            try {
                GlobalScope.launch(Dispatchers.IO) {
                    val link =
                        URL("https://api.openweathermap.org/geo/1.0/direct?q=$city&appid=504b4cc387a63ed21f767700acc62351")
                    if (link.readText() != "[]"){
                        val json = JSONArray(link.readText())
                        city2 = json.getJSONObject(0).getJSONObject("local_names").getString("uk")
                        city3 = json.getJSONObject(0).getString("name")

                        if (city2 in list){
                            runOnUiThread {
                                Toast.makeText(
                                    this@CitiesActivity,
                                    "Місто вже додано",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else{
                            val list2 = list.toList()
                            val cv = ContentValues()
                            cv.put("CITY", city2)
                            cv.put("CITYEN", city3)

                            db.insert("CITIES", null, cv)
                            list.add(city2)
                            if (!changeDB) changeDB = true
                            runOnUiThread {
                                adapter.notifyItemInserted(list.size)
                            }
                        }


                    }
                    else {
                        runOnUiThread {
                            Toast.makeText(
                                this@CitiesActivity,
                                "Місто не знайдено",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } catch (e: Exception) {Toast.makeText(this, "Місто не додано", Toast.LENGTH_LONG).show()}

        }
    }

}