package com.sviatkuzbyt.weatherapp.cities

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sviatkuzbyt.weatherapp.*
import com.sviatkuzbyt.weatherapp.DataBase
import com.sviatkuzbyt.weatherapp.cities.movingitems.SimpleItemTouchHelperCallback
import kotlinx.coroutines.*
import org.json.JSONArray
import java.net.URL

@DelicateCoroutinesApi
class CitiesActivity : AppCompatActivity() {

    lateinit var cursor: Cursor
    lateinit var db: SQLiteDatabase
    lateinit var adapter: CitiesListAdapter
    lateinit var citiesRecyclerView: RecyclerView
    val list = mutableListOf<String>()
    lateinit var firstList: List<String>

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

            firstList = list.toList()

        }catch (e: Exception) {Toast.makeText(this, "Все пизда", Toast.LENGTH_SHORT).show()}

        adapter = CitiesListAdapter(list, this)
        citiesRecyclerView.adapter = adapter
        citiesRecyclerView.layoutManager = LinearLayoutManager(this)

        val addBtn = findViewById<Button>(R.id.addBtn)
        addBtn.setOnClickListener {
            val bottomSheetDialog = AddCityFragment(this)
            bottomSheetDialog.show(supportFragmentManager, "add city")
        }

        val callback = SimpleItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(citiesRecyclerView)

        val backCities = findViewById<Button>(R.id.backCities)
        backCities.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (list != firstList){
            db.delete("CITIES", null, null)
            list.forEach {
                val cv = ContentValues()
                cv.put("CITY", it)
                db.insert("CITIES", null, cv)
            }
        }
        cursor.close()
        db.close()
    }

    fun deleteRecord(city: String){
        try {
//            adapter.notifyItemRemoved(id)
//            db.delete("CITIES", "CITY = ?", arrayOf(city))
            list.remove(city)
            adapter.notifyDataSetChanged()

        } catch (e: Exception){Toast.makeText(this, "Помилка видалення", Toast.LENGTH_SHORT).show()}

    }

    fun addRecord(city: String){

        if (city in list){
            Toast.makeText(this, "Місто вже додано", Toast.LENGTH_LONG).show()

        } else{
            var city2 = "None"
            GlobalScope.launch(Dispatchers.IO){
                try {
                    val link = URL("https://api.openweathermap.org/geo/1.0/direct?q=$city&appid=504b4cc387a63ed21f767700acc62351")
                    val json = JSONArray(link.readText())
                    city2 = json.getJSONObject(0).getJSONObject("local_names").getString("uk")
                } catch (e: Exception) {}

                if (city2 != "None"){
//                    val cv = ContentValues()
//                    cv.put("CITY", city2)
//                    db.insert("CITIES", null, cv)
                    list.add(city2)

                    runOnUiThread{
                        adapter.notifyItemInserted(list.size)
                    }
                }
                else{
                    runOnUiThread {
                        Toast.makeText(this@CitiesActivity, "Неправильна назва міста", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}