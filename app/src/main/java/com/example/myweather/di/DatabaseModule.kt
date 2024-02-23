package com.example.myweather.di

import android.content.Context
import androidx.room.Room
import com.example.myweather.data.locale.LocationDAO
import com.example.myweather.data.locale.LocationDataBase
import com.example.myweather.data.remote.api.WeatherApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLocationDatabase(@ApplicationContext context: Context): LocationDataBase {

        return Room.databaseBuilder(
            context,
            LocationDataBase::class.java,
            "location.db"
        ).build()


    }

    @Singleton
    @Provides
    fun provideLocationDao(database: LocationDataBase): LocationDAO {

        return database.locationDao()

    }

}