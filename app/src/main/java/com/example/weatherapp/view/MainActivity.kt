package com.example.weatherapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherInfoShowModel
import com.example.weatherapp.model.WeatherInfoShowModelImpl
import com.example.weatherapp.model.data_class.City
import com.example.weatherapp.model.data_class.WeatherData
import com.example.weatherapp.model.data_class.WeatherInfoResponse
import com.example.weatherapp.util.convertToListOfCityName
import com.example.weatherapp.viewmodel.WeatherInfoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_input_part.*
import kotlinx.android.synthetic.main.layout_sunrise_sunset.*
import kotlinx.android.synthetic.main.layout_weather_additional_info.*
import kotlinx.android.synthetic.main.layout_weather_basic_info.*



class MainActivity : AppCompatActivity() {

    private lateinit var model: WeatherInfoShowModel
    private lateinit var viewModel: WeatherInfoViewModel

    private var cityList: MutableList<City> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = WeatherInfoShowModelImpl(applicationContext)

        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application).create(WeatherInfoViewModel::class.java)



        setLiveDataListener()

        setViewClickListner()

        viewModel.getCityList(model)
    }

    private fun setLiveDataListener() {
        
        viewModel.cityListLiveData.observe(this, object : Observer<MutableList<City>> {
            override fun onChanged(t: MutableList<City>) {
               setCityListSpinner(t)
            }
        })


        viewModel.cityListFailureLiveData.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })


        viewModel.progressBarLiveData.observe(this, Observer { isShowLoader ->
            if(isShowLoader){
                progressBar.visibility = View.VISIBLE
            }else{
                progressBar.visibility = View.GONE
            }
        })

        viewModel.weatherInfoLiveData.observe(this, Observer { weatherData ->
            setWeatherInfo(weatherData)
        })

        viewModel.weatherInfoFailure.observe(this, Observer { errorMessage ->
            output_group.visibility = View.GONE
            tv_error_message.visibility = View.VISIBLE
            tv_error_message.text = errorMessage
        })
    }

    private fun setViewClickListner() {
        btn_view_weather.setOnClickListener {
            val selectedCityId = cityList[spinner.selectedItemPosition].id
            viewModel.getWeatherInfo(selectedCityId, model) // fetch weather info
        }
    }

    private fun setCityListSpinner(cityList: MutableList<City>) {
        this.cityList = cityList

        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            this.cityList.convertToListOfCityName()
        )

        spinner.adapter = arrayAdapter
    }

    private fun setWeatherInfo(weatherData: WeatherData) {
        output_group.visibility = View.VISIBLE
        tv_error_message.visibility = View.GONE

        tv_date_time?.text = weatherData.dateTime
        tv_temperature?.text = weatherData.temperature
        tv_city_country?.text = weatherData.cityAndCountry
        Glide.with(this).load(weatherData.weatherConditionIconUrl).into(iv_weather_condition)
        tv_weather_condition?.text = weatherData.weatherConditionIconDescription

        tv_humidity_value?.text = weatherData.humidity
        tv_pressure_value?.text = weatherData.pressure
        tv_visibility_value?.text = weatherData.visibility

        tv_sunrise_time?.text = weatherData.sunrise
        tv_sunset_time?.text = weatherData.sunset
    }
}