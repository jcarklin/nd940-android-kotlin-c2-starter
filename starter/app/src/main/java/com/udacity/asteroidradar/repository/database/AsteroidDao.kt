package com.udacity.asteroidradar.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.domain.Asteroid

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroids: List<AsteroidEntity>)

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()

    @Query("SELECT * FROM asteroid_table ORDER BY id DESC")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

}

