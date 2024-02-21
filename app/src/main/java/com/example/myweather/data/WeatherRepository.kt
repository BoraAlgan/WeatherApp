package com.example.myweather.data

import com.example.myweather.data.remote.api.WeatherApiClient
import com.example.myweather.data.remote.model.WeatherResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val serviceWeather: WeatherApiClient
){


    fun getWeather(query: String): Flow<WeatherResponseModel> {

        return flow {
            val response = serviceWeather.getWeather(query)
            emit(response)
        }

    }

    fun getWeatherWithCord(lat: Double, lon: Double): Flow<WeatherResponseModel> {

        return flow {
            val responseWithCord = serviceWeather.getWeatherWithCord(lat, lon)
            emit(responseWithCord)
        }
    }

}