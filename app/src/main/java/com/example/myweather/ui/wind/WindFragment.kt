package com.example.myweather.ui.wind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myweather.databinding.FragmentWindBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WindFragment : Fragment() {

    private val viewModel: WindViewModel by viewModels()
    private lateinit var binding: FragmentWindBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWindBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}