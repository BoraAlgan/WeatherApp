package com.example.myweather.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.FileObserver.ACCESS
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.navigation.NavHost
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myweather.R
import com.example.myweather.data.locale.LocationDataBase
import com.example.myweather.data.locale.SavedLocations
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.databinding.CustomDialogAlertBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        startLocationUpdates()

        addDataToDatabase()


    }

    fun addDataToDatabase() {
        val db = LocationDataBase.getLocationDatabase(this)
        val dao = db?.locationDao()
        dao?.insert(
            SavedLocations(
                savedLocationName = "Antalya"
            )
        )
        dao?.insert(
            SavedLocations(
                savedLocationName = "İstanbul"
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editLocation -> {

                alertDialogBuilder()

                Toast.makeText(this, "Choose the location you wanted", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun isLocationAvailable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        when {
            isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) && isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_LONG).show()

                if (isLocationAvailable(this)) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                    val locationTask: Task<Location> = fusedLocationClient.lastLocation

                    locationTask

                        .addOnSuccessListener { location ->
                            Toast.makeText(this, "Location obtained :).", Toast.LENGTH_SHORT).show()

                            val latitude = location.latitude
                            val longitude = location.longitude
                            println("" + latitude + "" + longitude)




                            if (Geocoder.isPresent()) {
                                val geocoder = Geocoder(this, Locale.getDefault())
                                val addresses: List<Address>? =
                                    geocoder.getFromLocation(latitude, longitude, 1)

                                if (!addresses.isNullOrEmpty()) {
                                    val city = addresses[0].adminArea
                                    val country = addresses[0].countryName
                                    val postalCode = addresses[0].postalCode

                                    viewModel.fetchWeatherWithCordData(
                                        lat = latitude,
                                        lon = longitude,
                                        locationName = city
                                    )

                                    println(city + country + postalCode)
                                }
                            } else {
                                Toast.makeText(this, "Unknown location.", Toast.LENGTH_SHORT).show()

                                //last ( geocoder datasına ulaşılamadığı zaman)
                            }

                        }

                        .addOnFailureListener { exception ->
                            //lat lon değerini alamadığımız case
                            Toast.makeText(
                                this,
                                "Location could not be obtained. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                } else {
                    Toast.makeText(this, "Turn on location.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            }

            isPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && isPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                Toast.makeText(this, "It needs to be allowed from the settings.", Toast.LENGTH_LONG).show()
                checkQuery()


            }

            else -> {
                Toast.makeText(this, "Lütfen izin verin", Toast.LENGTH_LONG).show()
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_REQUEST_CODE
                )
            }
        }

    }

    private fun isPermissionGranted(permission: String) = ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    //izin diyalogu cikmadigi durumlari ele almak icin kullanilir
    // ShouldShowRequestPermissionRationale -> istenen izin için ek diyalog açılması durumunu döner.
    private fun isPermissionRationale(permission: String) = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

    private fun checkQuery() {
        val city = getPreferences(CITY)
        if (city == null) {
            alertDialogBuilder()
        } else {
            viewModel.fetchWeatherData(city)
        }
    }

    private fun alertDialogBuilder() {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = CustomDialogAlertBinding.inflate(layoutInflater, null, false)

        with(dialogBinding) {
            dialogButton.setOnClickListener {
                val txt = cityEndpointer.text.toString()
                putPreferences(txt)
                viewModel.fetchWeatherData(txt)
                dialog.dismiss()
            }
        }
        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    private fun putPreferences(value: String) {
        preferences.edit {
            putString(CITY, value)
        }
    }

    private fun getPreferences(value: String): String? {
        return preferences.getString(value, null)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                Toast.makeText(this, "İzin verilmedi", Toast.LENGTH_LONG).show()

                checkQuery()

            } else {
                Toast.makeText(this, "İzinler verildi konum servisi kullanılmaya başlanıyor", Toast.LENGTH_LONG).show()
                startLocationUpdates()
            }
        }
    }

    companion object {
        private val LOCATION_REQUEST_CODE = 100
        private val CITY = "city"
    }
}
