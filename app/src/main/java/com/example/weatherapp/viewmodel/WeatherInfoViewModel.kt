package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.common.RequestCompleteListener
import com.example.weatherapp.model.WeatherInfoShowModel
import com.example.weatherapp.model.data_class.City
import com.example.weatherapp.model.data_class.WeatherData
import com.example.weatherapp.model.data_class.WeatherInfoResponse
import com.example.weatherapp.util.kelvinToCelsius
import com.example.weatherapp.util.unixTimestampToDateTimeString
import com.example.weatherapp.util.unixTimestampToTimeString

class WeatherInfoViewModel : ViewModel() {


    val cityListLiveData = MutableLiveData<MutableList<City>>()
    val cityListFailureLiveData = MutableLiveData<String>()
    val weatherInfoLiveData = MutableLiveData<WeatherData>()
    val weatherInfoFailure = MutableLiveData<String>()
    val progressBarLiveData = MutableLiveData<Boolean>()



    fun getCityList(model : WeatherInfoShowModel){
        model.getCityList(object : RequestCompleteListener<MutableList<City>>{
            override fun onRequestSuccess(data: MutableList<City>) {
                cityListLiveData.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
               cityListFailureLiveData.postValue(errorMessage)
            }

        })
    }

    fun getWeatherInfo(cityId: Int, model: WeatherInfoShowModel){
        progressBarLiveData.postValue(true)

        model.getWeatherInfo(cityId, object : RequestCompleteListener<WeatherInfoResponse>{
            override fun onRequestSuccess(data: WeatherInfoResponse) {
                val weatherData = WeatherData(
                    dateTime = data.dt.unixTimestampToDateTimeString(),
                    temperature = data.main.temp.kelvinToCelsius().toString(),
                    cityAndCountry = "${data.name}, ${data.sys.country}",
                    weatherConditionIconUrl = "http://openweathermap.org/img/w/${data.weather[0].icon}.png",
                    humidity = "${data.main.humidity}%",
                    pressure = "${data.main.pressure} mBar",
                    visibility = "${data.visibility/1000.0} KM",
                    sunrise = data.sys.sunrise.unixTimestampToTimeString(),
                    sunset = data.sys.sunset.unixTimestampToTimeString()

                )

                progressBarLiveData.postValue(false)

                weatherInfoLiveData.postValue(weatherData)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                weatherInfoFailure.postValue(errorMessage)
            }

        })
    }
}