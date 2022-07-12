package com.sviatkuzbyt.weatherapp

fun setWeatherImage(text: String): Int{
    return if (text == "01d") R.drawable.ic_weather_icon_01d
    else if ( text == "01n") R.drawable.ic_weather_icon_01n
    else if ( text == "02d") R.drawable.ic_weather_icon_02d
    else if ( text == "02n") R.drawable.ic_weather_icon_02n
    else if ("03" in text) R.drawable.ic_weather_icon_03dn
    else if ("04" in text) R.drawable.ic_weather_icon_04dn
    else if ("09" in text) R.drawable.ic_weather_icon_09dn
    else if ("10" in text) R.drawable.ic_weather_icon_10dn
    else if ("11" in text) R.drawable.ic_weather_icon_11dn
    else if ("13" in text) R.drawable.ic_weather_icon_13dn
    else if ("50" in text) R.drawable.ic_weather_icon_50dn
    else R.color.empty
}