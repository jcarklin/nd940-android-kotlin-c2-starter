package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidRepository

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = AsteroidRecyclerViewAdapter(AsteroidRecyclerViewAdapter.ClickListener {
            viewModel.navigateToSelectedAsteroid(it)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroidList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.data = it
            }
        })

        viewModel.navigateToAsteroid.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigateToSelectedAsteroidDone()
            }
        })

        viewModel.status.observe(viewLifecycleOwner, {
            it?. let {
                if (it == Status.ERROR) {
                    Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG).show()
                }
            }
        })
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_week_menu -> AsteroidRepository.NasaApiFilter.SHOW_THIS_WEEK
                R.id.show_today_menu -> AsteroidRepository.NasaApiFilter.SHOW_TODAY
                else -> AsteroidRepository.NasaApiFilter.SHOW_SAVED
            }
        )
        return true
    }
}
