package com.example.myweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.remote.domain.WeatherUseCase
import com.example.myweather.data.remote.model.WeatherResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(

    private val useCase: WeatherUseCase,

) : ViewModel() {

}