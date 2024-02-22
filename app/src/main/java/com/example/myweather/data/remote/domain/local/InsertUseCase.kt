package com.example.myweather.data.remote.domain.local

import com.example.myweather.data.WeatherLocalRepository
import com.example.myweather.data.WeatherRemoteRepository
import com.example.myweather.data.locale.SavedLocations
import com.example.myweather.data.remote.model.WeatherResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class InsertUseCase @Inject constructor(

    private val repository: WeatherLocalRepository
) {

    fun insertData(
        locations: SavedLocations,
        onFailure: (String) -> Unit
    ): Flow<Unit> {

        return repository.insertLocation(locations)
            .flowOn(Dispatchers.IO)
            .catch {
                onFailure(it.message.toString())
            }
    }



}