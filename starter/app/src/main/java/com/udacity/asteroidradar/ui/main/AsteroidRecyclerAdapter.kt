package com.udacity.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemViewBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidRecyclerViewAdapter(private val clickListener: ClickListener) :
    ListAdapter<Asteroid, AsteroidViewHolder>(DiffCallback) {

    companion object DiffCallback  : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    var data = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = data[position]
        holder.binding.asteroid = item
        holder.binding.asteroidLayout.setOnClickListener {
            clickListener.onClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AsteroidItemViewBinding.inflate(layoutInflater, parent, false)
        return AsteroidViewHolder(binding)
    }

    class ClickListener(val clickListener: (asteroid: Asteroid)->Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}

class AsteroidViewHolder (internal val binding: AsteroidItemViewBinding)
    : RecyclerView.ViewHolder(binding.root)