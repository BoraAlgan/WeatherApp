package com.example.myweather.ui.location

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myweather.R
import com.example.myweather.data.locale.SavedLocations
import com.example.myweather.databinding.CustomDialogAlertBinding
import com.example.myweather.databinding.FragmentLocationBinding
import com.example.myweather.ui.location.adapter.LocationAdapter
import com.example.myweather.ui.weather.WeatherFragment
import com.example.myweather.util.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment : Fragment() {

    private val viewModel: LocationViewModel by viewModels()
    private lateinit var binding: FragmentLocationBinding

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
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllLocations()

        viewModel.locationsList.observe(viewLifecycleOwner) { list ->
            val adapter = LocationAdapter(
                list = list,
                onCardClick = { navigateLocationName ->

                    putPreferences(navigateLocationName)
                    findNavController().navigateUp()

                },
                onDeleteClick = { location ->
                    viewModel.deleteLocation(location)
                }
            )

            binding.rvLocation.adapter = adapter
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun alertDialogBuilder() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding = CustomDialogAlertBinding.inflate(layoutInflater, null, false)

        with(dialogBinding) {
            dialogButton.setOnClickListener {
                val txt = cityEndpointer.text.toString()
                viewModel.insertUseCase(
                    SavedLocations(savedLocationName = txt)
                )
                dialog.dismiss()
                NotificationUtil().createNotification(requireContext(), "31", "LocationObserve")
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_location_toolbar, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addLocation -> {

                alertDialogBuilder()

                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }


    companion object {
        private val CITY = "city"
    }
}