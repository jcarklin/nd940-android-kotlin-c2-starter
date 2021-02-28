package com.udacity.asteroidradar.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
        holder.bind(clickListener, data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    class ClickListener(val clickListener: (asteroid: Asteroid)->Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}

class AsteroidViewHolder private constructor(private val binding: AsteroidItemViewBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: AsteroidRecyclerViewAdapter.ClickListener, item: Asteroid) {
        binding.asteroid = item
        binding.asteroidLayout.setOnClickListener {
            clickListener.onClick(item)
        }
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AsteroidViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AsteroidItemViewBinding.inflate(layoutInflater, parent, false)
            return AsteroidViewHolder(binding)
        }
    }
}