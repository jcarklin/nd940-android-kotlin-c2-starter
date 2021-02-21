package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.api.NasaApi
import com.udacity.asteroidradar.repository.api.NasaApiService
import com.udacity.asteroidradar.repository.api.asDatabaseModel
import com.udacity.asteroidradar.repository.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.database.asDomainModel
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations
            .map(database.asteroidDao.getAllAsteroids()) { it.asDomainModel() }


    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val asteroidList = parseAsteroidsJsonResult(JSONObject(getAsteroids()))
            database.asteroidDao.insert(asteroidList.asDatabaseModel())
        }
    }

    private suspend fun getAsteroids() : String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val start = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
        val end = dateFormat.format(calendar.time)

        return NasaApi.retrofitService.getAsteroids(start, end, BuildConfig.NASA_KEY)
    }

}