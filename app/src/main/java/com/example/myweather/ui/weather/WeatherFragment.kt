package com.example.myweather.ui.weather

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
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myweather.R
import com.example.myweather.databinding.CustomDialogAlertBinding
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.extentions.capitalizeWords
import com.example.myweather.extentions.epochToDateTime
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: FragmentWeatherBinding

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner) {
            with(binding) {
                weatherTemp.text = getString(R.string.celcius_temp, it.main.temp.roundToInt().toString())
                minTemp.text = getString(R.string.min_temp, it.main.tempMin.roundToInt().toString())
                maxTemp.text = getString(R.string.max_temp, it.main.tempMax.roundToInt().toString())
                location.text = it.name
                weatherStatus.text = it.weather.firstOrNull()?.description?.capitalizeWords() ?: "Clean Weather"
                text2.text = getString(R.string.updated_time, updatedTime())


                sunriseTime.text = it.sys.sunrise.epochToDateTime()
                sunsetTime.text = it.sys.sunset.epochToDateTime()
                windTime.text = it.wind.speed.toString()
                pressureTime.text = it.main.pressure.toString()
                humidityTime.text = it.main.humidity.toString()
                customTime.text = it.main.seaLevel.toString()

            }
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) {

//            Toast.makeText(requireContext(),"Please enter a valid location.", Toast.LENGTH_SHORT).show()
        }
        startLocationUpdates()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_weather_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editLocation -> {

                alertDialogBuilder()

                Toast.makeText(requireContext(), "Choose the location you wanted", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun alertDialogBuilder() {
        val dialog = AlertDialog.Builder(requireContext()).create()
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

    @SuppressLint("SimpleDateFormat")
    private fun updatedTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH.mm - dd.MM.yyyy")

        return formatter.format(time)
    }

    private fun isLocationAvailable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        when {
            isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) && isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                Toast.makeText(requireContext(), "Location permission granted", Toast.LENGTH_LONG).show()

                if (isLocationAvailable(requireContext())) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

                    val locationTask: Task<Location> = fusedLocationClient.lastLocation

                    locationTask

                        .addOnSuccessListener { location ->
                            Toast.makeText(requireContext(), "Location obtained :).", Toast.LENGTH_SHORT).show()

                            val latitude = location.latitude
                            val longitude = location.longitude
                            println("" + latitude + "" + longitude)




                            if (Geocoder.isPresent()) {
                                val geocoder = Geocoder(requireContext(), Locale.getDefault())
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
                                Toast.makeText(requireContext(), "Unknown location.", Toast.LENGTH_SHORT).show()

                                //last ( geocoder datasına ulaşılamadığı zaman)
                            }

                        }

                        .addOnFailureListener { exception ->
                            //lat lon değerini alamadığımız case
                            Toast.makeText(
                                requireContext(),
                                "Location could not be obtained. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                } else {
                    Toast.makeText(requireContext(), "Turn on location.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            }

            isPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && isPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                checkQuery()
            }

            else -> {
                Toast.makeText(requireContext(), "Lütfen izin verin", Toast.LENGTH_LONG).show()
                requestPermissions(
                    arrayOf(

                    ),
                    LOCATION_REQUEST_CODE
                )
            }
        }

    }

    private fun checkQuery() {
        val city = getPreferences(CITY)
        if (city == null) {
            alertDialogBuilder()
        } else {
            viewModel.fetchWeatherData(city)
        }
    }

    private fun isPermissionGranted(permission: String) = ActivityCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

    //izin diyalogu cikmadigi durumlari ele almak icin kullanilir
    // ShouldShowRequestPermissionRationale -> istenen izin için ek diyalog açılması durumunu döner.
    private fun isPermissionRationale(permission: String) = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                Toast.makeText(requireContext(), "İzin verilmedi", Toast.LENGTH_LONG).show()

                checkQuery()

            } else {
                Toast.makeText(requireContext(), "İzinler verildi konum servisi kullanılmaya başlanıyor", Toast.LENGTH_LONG).show()
                startLocationUpdates()
            }
        }
    }


    companion object {
        private val LOCATION_REQUEST_CODE = 100
        private val CITY = "city"
    }
}