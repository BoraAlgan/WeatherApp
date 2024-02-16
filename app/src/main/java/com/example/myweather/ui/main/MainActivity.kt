package com.example.myweather.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.FileObserver.ACCESS
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.navigation.NavHost
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myweather.R
import com.example.myweather.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel.fetchWeatherData("İstanbul")
//        viewModel.fetchWeatherWithCordData(10.55, 10.7)

        //bottom nav

        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHost

        val navController = navHost.navController

        binding.bottomNavigation.setupWithNavController(navController)

        setSupportActionBar(binding.toolbar)

        val appConfiguration = AppBarConfiguration(
            setOf(
                R.id.weatherFragment,
                R.id.windFragment,
                R.id.rainandcloudFragment
            )
        )

        setupActionBarWithNavController(navController, appConfiguration)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)


    }


    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun isLocationAvailable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (isLocationAvailable(this)) {

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                val locationTask: Task<Location> = fusedLocationClient.lastLocation

                locationTask.addOnSuccessListener { location ->

                    Toast.makeText(this, "Location obtained :).", Toast.LENGTH_SHORT).show()

                    val latitude = location.latitude
                    val longitude = location.longitude
                    println("" + latitude + "" + longitude)

                    if (Geocoder.isPresent()) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses: List<Address>? =
                            geocoder.getFromLocation(latitude, longitude, 1)

                        if (!addresses.isNullOrEmpty()) {
                            val city = addresses[0].locality
                            val country = addresses[0].countryName
                            val postalCode = addresses[0].postalCode

                            println(city + country + postalCode)

                        }
                    } else {

                        Toast.makeText(this, "Unknown location.", Toast.LENGTH_SHORT).show()

                        //last
                    }

                }.addOnFailureListener { exception ->

                    Toast.makeText(
                        this,
                        "Location could not be obtained. Please try again",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)

                }

            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

            return
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
        }


    }
}
