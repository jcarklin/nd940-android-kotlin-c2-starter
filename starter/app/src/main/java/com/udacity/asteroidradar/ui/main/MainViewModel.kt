package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.AsteroidRepository.NasaApiFilter
import com.udacity.asteroidradar.repository.database.AsteroidDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AsteroidRepository(AsteroidDatabase.getInstance(application))

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> get() = _status

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid: LiveData<Asteroid?> get() = _navigateToAsteroid

    val asteroidList = repository.asteroids
    val imageOfDay = repository.pictureOfDay

    init {
        viewModelScope.launch {
            try {
                _status.value = Status.LOADING
                repository.refresh()
                _status.value = Status.DONE
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
            repository.filterBy(NasaApiFilter.SHOW_SAVED)
        }
    }

    fun updateFilter(filter: NasaApiFilter) {
        viewModelScope.launch {
            repository.filterBy(filter)
        }
    }

    fun navigateToSelectedAsteroid(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun navigateToSelectedAsteroidDone() {
        _navigateToAsteroid.value = null
    }
}

enum class Status {
    LOADING, ERROR, DONE
}