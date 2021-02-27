package com.udacity.asteroidradar.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.domain.Asteroid
import java.util.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAsteroids(asteroids: List<AsteroidEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDay(pod: PictureOfDayEntity)

    @Query("DELETE FROM asteroid_table")
    suspend fun clearAsteroids()

    @Query("DELETE FROM picture_of_day")
    suspend fun clearPoD()

    @Query("SELECT * FROM asteroid_table WHERE approach_date BETWEEN :start AND :end ORDER BY approach_date")
    fun getAllAsteroidsBetweenDates(start: Date, end: Date): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM picture_of_day")
    fun getPictureOfDay(): LiveData<PictureOfDayEntity?>
}

