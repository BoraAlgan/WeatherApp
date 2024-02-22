package com.example.myweather.ui.location.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.data.locale.SavedLocations
import com.example.myweather.databinding.ItemviewLocationBinding

class LocationAdapter(
    val list: List<SavedLocations>,
    val onCardClick: (String) -> Unit,
    val onDeleteClick: (SavedLocations) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    inner class LocationViewHolder(private val binding: ItemviewLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SavedLocations) {

            binding.locationCardView.setOnClickListener {
                onCardClick.invoke(item.savedLocationName)
            }
            binding.imageDelete.setOnClickListener {
                onDeleteClick.invoke(item)
            }
            binding.txtLocation.text = item.savedLocationName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemviewLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LocationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }


}