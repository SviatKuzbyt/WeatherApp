package com.sviatkuzbyt.weatherapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FiveDaysListAdapter(val dataSet: MutableList<WeatherOf5DayList>, val context: Context) :
    RecyclerView.Adapter<FiveDaysListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView
        val date: TextView
        val weatherForDay: RecyclerView

        init {
            // Define click listener for the ViewHolder's View.
            day = view.findViewById(R.id.day)
            date = view.findViewById(R.id.date)
            weatherForDay = view.findViewById(R.id.weatherForDay)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.weather_info_for_5_days, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.day.text = dataSet[position].day
        viewHolder.date.text = dataSet[position].date
        val adapter = DayListAdapter(dataSet[position].weather, true)

        viewHolder.weatherForDay.adapter = adapter
        viewHolder.weatherForDay.layoutManager = LinearLayoutManager(context)
        viewHolder.weatherForDay.isNestedScrollingEnabled = false

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}