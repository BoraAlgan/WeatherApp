package com.example.myweather.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedLocations::class], version = 1)
abstract class LocationDataBase : RoomDatabase() {
    abstract fun locationDao(): LocationDAO

    companion object {
        private var instance: LocationDataBase? = null

        fun getLocationDatabase(context: Context): LocationDataBase? {

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    LocationDataBase::class.java,
                    "location.db"
                ).build()
            }



            return instance
        }
    }

}