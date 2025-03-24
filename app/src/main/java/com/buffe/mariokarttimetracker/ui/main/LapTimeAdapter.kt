package com.buffe.mariokarttimetracker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buffe.mariokarttimetracker.databinding.ItemLapTimeBinding

class LapTimeAdapter(private val lapTimes: List<LapTime>) :
    RecyclerView.Adapter<LapTimeAdapter.LapTimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapTimeViewHolder {
        val binding = ItemLapTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LapTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LapTimeViewHolder, position: Int) {
        val lapTime = lapTimes[position]
        holder.binding.tvLapNumber.text = "Runde ${lapTime.lapNumber}"
        holder.binding.tvLapTime.text = lapTime.getFormattedLapTime()
    }

    override fun getItemCount() = lapTimes.size

    inner class LapTimeViewHolder(val binding: ItemLapTimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}
