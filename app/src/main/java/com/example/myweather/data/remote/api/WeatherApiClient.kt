package com.example.myweather.data.remote.api

import com.example.myweather.data.remote.model.WeatherResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiClient {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") query: String?,
        @Query("units") units: String = "metric"
    ): WeatherResponseModel

    @GET("weather")
    suspend fun  getWeatherWithCord(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?
    ): WeatherResponseModel


   //https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
   // https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
}
