package com.example.myweather.ui.rainCloud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myweather.databinding.FragmentRainAndCloudBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RainAndCloudFragment : Fragment() {

    private val viewModel: RainAndCloudViewModel by viewModels()
    private lateinit var binding: FragmentRainAndCloudBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRainAndCloudBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}