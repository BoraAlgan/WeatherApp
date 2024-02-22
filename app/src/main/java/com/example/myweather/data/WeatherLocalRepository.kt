package com.example.myweather.data

import com.example.myweather.data.locale.LocationDAO
import com.example.myweather.data.locale.SavedLocations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherLocalRepository @Inject constructor(
    private val locationDAO: LocationDAO
){




    fun insertLocation(location: SavedLocations): Flow<Unit> {

        return flow {
            locationDAO.insert(location)
        }

    }

    fun deleteLocation(location: SavedLocations): Flow<Unit> {

        return flow {
            locationDAO.delete(location)
        }

    }

    fun getAllLocations() : Flow<List<SavedLocations>> {

        return flow {
            val response = locationDAO.getAll()
            emit(response)
        }

    }

}

