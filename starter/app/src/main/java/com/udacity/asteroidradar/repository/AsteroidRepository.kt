package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.api.NasaApi
import com.udacity.asteroidradar.repository.api.asDatabaseModel
import com.udacity.asteroidradar.repository.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.database.asDomainModel
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    enum class NasaApiFilter(val value: String) {
        SHOW_TODAY("today"), SHOW_THIS_WEEK("week"), SHOW_SAVED("saved")
    }

    private val _filterBy = MutableLiveData<NasaApiFilter>()
    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(
        _filterBy) { filter ->
        val calendar = Calendar.getInstance()
        val start = calendar.time
        if (filter==NasaApiFilter.SHOW_THIS_WEEK) {
            //saturday is last day of week
            var dayOfWeek = Calendar.SATURDAY-calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_YEAR, dayOfWeek)
        } else if (filter!=NasaApiFilter.SHOW_TODAY) {
            calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
        }
        val end = calendar.time
        Transformations.map(database.asteroidDao.getAllAsteroidsBetweenDates(start, end)) {
            it?.asDomainModel()
        }
    }
    val pictureOfDay: LiveData<PictureOfDay> = Transformations
        .map(database.asteroidDao.getPictureOfDay()) { it?.asDomainModel() }


    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val asteroidList = parseAsteroidsJsonResult(JSONObject(getAsteroids()))
            database.asteroidDao.insertAsteroids(asteroidList.asDatabaseModel())
            val pictureOfDay = getPictureOfDay()
            database.asteroidDao.insertPictureOfDay(pictureOfDay.asDatabaseModel())
        }
    }

    suspend fun filterBy(filter: NasaApiFilter) {
        if(asteroids.value.isNullOrEmpty()) {
            refresh()
        }
        _filterBy.value = filter
    }

    private suspend fun getAsteroids() : String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val start = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
        val end = dateFormat.format(calendar.time)

        return NasaApi.retrofitService.getAsteroids(start, end, BuildConfig.NASA_KEY)
    }

    private suspend fun getPictureOfDay(): PictureOfDay {
        var pictureOfDay = NasaApi.retrofitServiceMoshi.getPictureOfTheDay(BuildConfig.NASA_KEY)
        return pictureOfDay
    }

}