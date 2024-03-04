package com.example.myweather.ui.main

import androidx.lifecycle.ViewModel
import com.example.myweather.data.remote.domain.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    //  private val repository: WeatherRepository,
    private val useCase: WeatherUseCase

) : ViewModel() {


}