package com.example.weatherapp.util

import com.example.weatherapp.model.data_class.City
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun Int.unixTimestampToDateTimeString() : String{

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this*1000.toLong()

        val outputDateFormat = SimpleDateFormat(" dd MM, yyyy - hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)
    }catch (e: Exception){
        e.printStackTrace()
    }

    return this.toString()
}


fun Int.unixTimestampToTimeString() : String    {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this*1000.toLong()

        val outputDataFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        outputDataFormat.timeZone = TimeZone.getDefault()
        return outputDataFormat.format(calendar.time)
    }catch (e: IOException){
        e.printStackTrace()
    }
    return this.toString()
}

fun MutableList<City>.convertToListOfCityName() : MutableList<String> {

    val cityNameList: MutableList<String> = mutableListOf()

    for (city in this) {
        cityNameList.add(city.name)
    }

    return  cityNameList
}

/**
 * The temperature T in degrees Celsius (°C) is equal to the temperature T in Kelvin (K) minus 273.15:
 * T(°C) = T(K) - 273.15
 *
 * Example
 * Convert 300 Kelvin to degrees Celsius:
 * T(°C) = 300K - 273.15 = 26.85 °C
 */
fun Double.kelvinToCelsius() : Int {

    return  (this - 273.15).toInt()
}