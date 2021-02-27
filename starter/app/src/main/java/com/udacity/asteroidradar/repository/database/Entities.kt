package com.udacity.asteroidradar.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.util.Constants
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "asteroid_table")
data class AsteroidEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "code_name")
    val codename: String,
    @ColumnInfo(name = "approach_date")
    val closeApproachDate: Date,
    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,
    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "potentially_hazardous")
    val isPotentiallyHazardous: Boolean,
    @ColumnInfo(name = "saved_asteroid", defaultValue = "false")
    val isSaved: Boolean
)

@Entity(tableName = "picture_of_day")
data class PictureOfDayEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)

fun List<AsteroidEntity>.asDomainModel() : List<Asteroid> {
    val converters = Converters()
    return map {
        Asteroid(it.id, it.codename, converters.dateToString(it.closeApproachDate), it.absoluteMagnitude,
            it.estimatedDiameter, it.relativeVelocity, it.distanceFromEarth,
            it.isPotentiallyHazardous, it.isSaved)
    }
}

fun PictureOfDayEntity.asDomainModel() : PictureOfDay {
    return PictureOfDay(mediaType, title, url)
}

class Converters {
    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    @TypeConverter
    fun fromString(value: String): Date {
        return dateFormat.parse(value)
    }

    @TypeConverter
    fun dateToString(date: Date): String {
        return dateFormat.format(date)
    }
}