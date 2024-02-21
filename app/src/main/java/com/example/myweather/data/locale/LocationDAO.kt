package com.example.myweather.data.locale

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface LocationDAO {
    @Insert
    fun insert (location: SavedLocations)

    @Delete
    fun delete (locations: SavedLocations)


//    @Query()
//    fun getAll()
}