package com.sviatkuzbyt.weatherapp

data class WeatherOfDayList (val time: String, val img: Int, val temp: Int, val windSpeed: String)
data class WeatherOf5DayList (val day: String, val date: String, val weather: MutableList<WeatherOfDayList>)