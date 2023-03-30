package com.mihaelmarjanovic.dailylog23.database

import androidx.room.*
import com.mihaelmarjanovic.dailylog23.models.Day
import com.mihaelmarjanovic.dailylog23.models.Logs

@Dao
interface DayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(day: Day)

    @Delete
    suspend fun delete(day: Day)

    @Query("SELECT rating, id, date FROM day WHERE date = :date")
    suspend fun getDayRating(date: String): Day

    @Query("SELECT rating, id, date FROM day")
    suspend fun getAllDays(): List<Day>

    @Query("UPDATE day Set rating = :rating WHERE id = :id")
    suspend fun update(id: Int?, rating: Float?)
}