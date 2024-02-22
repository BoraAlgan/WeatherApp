package com.example.myweather.ui.location

import LocationViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myweather.databinding.FragmentLocationBinding
import com.example.myweather.ui.location.adapter.LocationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : Fragment() {

    private val viewModel: LocationViewModel by viewModels()
    private lateinit var binding: FragmentLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.locationsList.observe(viewLifecycleOwner) { list ->
            val adapter = LocationAdapter(
                list = list,
                onCardClick = {},
                onDeleteClick = {
                    viewModel.deleteUseCase(it)
                }
            )
        }

    }

}