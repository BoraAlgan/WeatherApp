package com.example.myweather.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.remote.domain.remote.WeatherUseCase
import com.example.myweather.data.remote.model.WeatherResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    //  private val repository: WeatherRepository,
    private val useCase: WeatherUseCase

) : ViewModel() {


}