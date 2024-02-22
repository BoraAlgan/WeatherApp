package com.example.myweather.ui.weather

import androidx.lifecycle.ViewModel
import com.example.myweather.data.remote.domain.remote.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(

    private val useCase: WeatherUseCase,

    ) : ViewModel() {

}