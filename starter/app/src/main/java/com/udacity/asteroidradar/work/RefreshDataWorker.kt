package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.database.AsteroidDatabase

class RefreshDataWorker(appContext: Context, parameters: WorkerParameters) :
    CoroutineWorker(appContext, parameters) {

    companion object {
        const val WORK_NAME = "RefreshData"
    }

    override suspend fun doWork(): Result {
        // TODO: clear previous records
        val repository = AsteroidRepository(AsteroidDatabase.getInstance(applicationContext))
        return try {
            repository.refresh()
            Result.success()
        } catch (e: Exception){
            e.printStackTrace()
            Result.retry()
        }
    }

}