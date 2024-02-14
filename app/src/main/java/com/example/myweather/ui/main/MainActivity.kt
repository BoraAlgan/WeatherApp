package com.example.myweather.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.example.myweather.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel.fetchWeatherData("İstanbul")
//        viewModel.fetchWeatherWithCordData(10.55, 10.7)






        //bottom nav
        val navHost = binding.fragmentContainer.getFragment() as NavHost

        //val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHost (Optional)

        val navController = navHost.navController

        binding.bottomNavigation.setupWithNavController(navController)


    }


}