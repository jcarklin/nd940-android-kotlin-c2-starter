package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.repository.api.NasaApi
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.database.AsteroidDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AsteroidRepository(AsteroidDatabase.getInstance(application))

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> get() = _status

    private val _imageOfTheDay = MutableLiveData<PictureOfDay>()
    val imageOfTheDay: LiveData<PictureOfDay> get() = _imageOfTheDay

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()
    val navigateToAsteroid: LiveData<Asteroid> get() = _navigateToAsteroid

    val asteroidList = repository.asteroids

    init {
        getPictureOfTheDay()
        viewModelScope.launch {
            repository.refresh()
        }
    }

    fun navigateToSelectedAsteroid(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun navigateToSelectedAsteroidDone() {
        _navigateToAsteroid.value = null
    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                _status.value = Status.LOADING
                val iotd = NasaApi.retrofitServiceMoshi.getPictureOfTheDay(BuildConfig.NASA_KEY)
                _imageOfTheDay.value = iotd
                _status.value = Status.DONE
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
        }
    }
}

enum class Status {
    LOADING, ERROR, DONE
}