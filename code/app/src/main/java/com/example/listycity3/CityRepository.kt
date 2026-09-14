package com.example.listycity3

import androidx.compose.runtime.mutableStateListOf

class CityRepository {

    // state data can change while our app is running compared to just "listOf" which does not update
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )
    // add a city to the list
    fun addCity(city: City) {
        _cities.add(city)
    }
    val cities: List<City>
        get() = _cities
}