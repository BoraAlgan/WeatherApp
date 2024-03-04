package com.example.myweather.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.locale.SavedLocations
import com.example.myweather.data.locale.domain.DeleteUseCase
import com.example.myweather.data.locale.domain.GetAllUseCase
import com.example.myweather.data.locale.domain.InsertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getAllUseCase: GetAllUseCase,
    private val insertUseCase: InsertUseCase,
    private val deleteUseCase: DeleteUseCase

) : ViewModel() {

    private val _locationsList = MutableLiveData<List<SavedLocations>>()
    val locationsList : LiveData<List<SavedLocations>> = _locationsList

    fun getAllLocations() {
        getAllUseCase.getAllData(
            onFailure = {

            },
            onSuccess = {
                _locationsList.value = it
            }
        )
            .launchIn(viewModelScope)
    }

    fun insertUseCase(location: SavedLocations) {
        insertUseCase.insertData(
            locations = location,
            onFailure = {},
            onSuccess = {
                getAllLocations()
            }
        )
            .launchIn(viewModelScope)

    }

    fun deleteLocation(location: SavedLocations) {
        deleteUseCase.deleteData(
            locations = location,
            onFailure = {},
            onSuccess = {
                getAllLocations()
            }
        )
            .launchIn(viewModelScope)
    }

}