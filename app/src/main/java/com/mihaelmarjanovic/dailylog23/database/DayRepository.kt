package com.mihaelmarjanovic.dailylog23.database

import com.mihaelmarjanovic.dailylog23.models.Day
import com.mihaelmarjanovic.dailylog23.models.Logs

class DayRepository(private val dayDao: DayDao) {
    suspend fun getDayRating(date: String): Day{
        return dayDao.getDayRating(date)
    }

    suspend fun getAllDays(): List<Day>{
        return dayDao.getAllDays()
    }


    suspend fun insert(day: Day){
        dayDao.insert(day)
    }

    suspend fun delete(day: Day){
        dayDao.delete(day)
    }

    suspend fun update(day: Day){
        dayDao.update(day.id, day.rating)
    }
}