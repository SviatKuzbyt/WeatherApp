package com.sviatkuzbyt.weatherapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView


class DayListAdapter(val dataSet: MutableList<WeatherOfDayList>, val blackMode: Boolean) :
    RecyclerView.Adapter<DayListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeShort: TextView
        val tempShort: TextView
        val windShort: TextView
        val imageShort: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            timeShort = view.findViewById(R.id.timeShort)
            tempShort = view.findViewById(R.id.tempShort)
            windShort = view.findViewById(R.id.windShort)
            imageShort = view.findViewById(R.id.imageShort)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.short_weather_info, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.timeShort.text = dataSet[position].time
        viewHolder.tempShort.text = "${dataSet[position].temp}°C"
        viewHolder.windShort.text = dataSet[position].windSpeed + "м/с"
        viewHolder.imageShort.setImageResource(dataSet[position].img)

        if (blackMode){
            viewHolder.timeShort.setTextColor(Color.BLACK)
            viewHolder.tempShort.setTextColor(Color.BLACK)
            viewHolder.windShort.setTextColor(Color.BLACK)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}