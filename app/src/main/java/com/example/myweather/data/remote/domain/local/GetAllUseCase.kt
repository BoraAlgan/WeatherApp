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


class GetAllUseCase @Inject constructor(

    private val repository: WeatherLocalRepository
) {

    fun getAllData(
        onFailure: (String) -> Unit,
        onSuccess: (List<SavedLocations>) -> Unit
    ): Flow<List<SavedLocations>> {

        return repository.getAllLocations()
            .flowOn(Dispatchers.IO)
            .catch {
                onFailure(it.message.toString())
            }
            .onEach {
                onSuccess.invoke(it)
            }
    }

}