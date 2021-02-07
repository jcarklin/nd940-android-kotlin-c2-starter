package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _imageOfTheDay = MutableLiveData<PictureOfDay>()
    val imageOfTheDay: LiveData<PictureOfDay> get() = _imageOfTheDay

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()
    val navigateToAsteroid: LiveData<Asteroid> get() = _navigateToAsteroid

    init {
        getAsteroids()
    }

    private fun getAsteroids() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val start = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
        val end = dateFormat.format(calendar.time)

        viewModelScope.launch {
            try {
                _status.value = Status.LOADING
                val iotd = NasaApi.retrofitServiceMoshi.getPictureOfTheDay(BuildConfig.NASA_KEY)
                _imageOfTheDay.value = iotd
                val response = NasaApi.retrofitService.getAsteroids(start, end,
                        BuildConfig.NASA_KEY)
                _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response))
                _status.value = Status.DONE
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
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