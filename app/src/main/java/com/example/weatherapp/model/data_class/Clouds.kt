package com.example.weatherapp.model.data_class

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all : Int = 0
)