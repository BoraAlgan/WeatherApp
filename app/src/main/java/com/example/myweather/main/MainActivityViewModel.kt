package com.example.myweather.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.WeatherRepository
import com.example.myweather.data.remote.model.WeatherResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    val liveData = MutableLiveData<WeatherResponseModel>()

     fun fetchWeatherData(query: String) {

        repository.getWeather(query)
            .flowOn(Dispatchers.IO)
            .catch {
                println("DEFAULT ERROR" + it.message)
            }
            .onEach {
                liveData.value = it
            }
            .launchIn(viewModelScope)
    }

    fun fetchWeatherWithCordData(lat: Double, lon: Double) {

        repository.getWeatherWithCord(lat, lon)
            .flowOn(Dispatchers.IO)
            .catch {
                println("CORD ERROR" + it.message)
            }
            .onEach {
                liveData.value = it
            }
            .launchIn(viewModelScope)
    }
}