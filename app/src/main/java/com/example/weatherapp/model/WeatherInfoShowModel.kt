package com.example.weatherapp.model

import com.example.weatherapp.common.RequestCompleteListener
import com.example.weatherapp.model.data_class.City
import com.example.weatherapp.model.data_class.WeatherInfoResponse

interface WeatherInfoShowModel {
    fun getCityList(callback: RequestCompleteListener<MutableList<City>>)
    fun getWeatherInfo(cityId: Int, callback: RequestCompleteListener<WeatherInfoResponse>)
}