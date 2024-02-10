package com.example.myweather.data.remote.domain

import com.example.myweather.data.WeatherRepository
import com.example.myweather.data.remote.model.WeatherResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class WeatherUseCase @Inject constructor(

    private val repository: WeatherRepository
) {
    fun fetchWeatherData(
        query: String,
        onSuccess: (WeatherResponseModel) -> Unit,
        onFailure: (String) -> Unit
    ): Flow<WeatherResponseModel> {

        return repository.getWeather(query)
            .flowOn(Dispatchers.IO)
            .catch {
                onFailure(it.message.toString())
            }
            .onEach {
                onSuccess.invoke(it)
            }
    }

    fun fetchWeatherWithCordData(
        lat: Double,
        lon: Double,
        onSuccess: (WeatherResponseModel) -> Unit,
        onFailure: (String) -> Unit
    ): Flow<WeatherResponseModel> {

        return repository.getWeatherWithCord(lat, lon)
            .flowOn(Dispatchers.IO)
            .catch {
                onFailure(it.message.toString())
            }
            .onEach {
                onSuccess.invoke(it)
            }
    }

}