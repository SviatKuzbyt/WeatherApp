package com.sviatkuzbyt.weatherapp.cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sviatkuzbyt.weatherapp.R



class CitiesListAdapter(val dataSet: MutableList<String>, val activity: CitiesActivity) :
    RecyclerView.Adapter<CitiesListAdapter.ViewHolder>() {
        /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val city: TextView
        val deleteBtn: Button


        init {
            // Define click listener for the ViewHolder's View.
            city = view.findViewById(R.id.cityNameSelect)
            deleteBtn = view.findViewById(R.id.deleteBtn)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cities_list, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.city.text = dataSet[position]
        viewHolder.deleteBtn.setOnClickListener {
            activity.deleteRecord(dataSet[position])
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}