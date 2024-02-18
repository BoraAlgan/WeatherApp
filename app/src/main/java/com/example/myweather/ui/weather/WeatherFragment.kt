package com.example.myweather.ui.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.extentions.capitalizeWords
import com.example.myweather.extentions.epochToDateTime
import com.example.myweather.ui.main.MainActivity
import com.example.myweather.ui.main.MainActivityViewModel
import com.example.myweather.ui.rainCloud.RainAndCloudViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt


@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var binding: FragmentWeatherBinding

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

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH.mm - dd.MM.yyyy")
        val current = formatter.format(time)


        activityViewModel.liveData.observe(viewLifecycleOwner) {
            with(binding) {
                weatherTemp.text = getString(R.string.celcius_temp, it.main.temp.roundToInt().toString())
                minTemp.text = getString(R.string.min_temp, it.main.tempMin.roundToInt().toString())
                maxTemp.text = getString(R.string.max_temp, it.main.tempMax.roundToInt().toString())
                location.text = it.name
                weatherStatus.text = it.weather.firstOrNull()?.description?.capitalizeWords() ?: "Clean Weather"
                text2.text = getString(R.string.updated_time, current)


                sunriseTime.text = it.sys.sunrise.epochToDateTime()
                sunsetTime.text = it.sys.sunset.epochToDateTime()
                windTime.text = it.wind.speed.toString()
                pressureTime.text = it.main.pressure.toString()
                humidityTime.text = it.main.humidity.toString()
                customTime.text = it.main.seaLevel.toString()
            }
        }
    }
}