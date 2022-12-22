package com.mihaelmarjanovic.dailylog23.database

import com.mihaelmarjanovic.dailylog23.models.Goals
import com.mihaelmarjanovic.dailylog23.models.Logs

class GoalsRepository(private val goalsDao: GoalsDao) {
    suspend fun getAllGoals(date: String): List<Goals>{
        return goalsDao.getAllGoals(date)
    }

    suspend fun updateCheckedGoal(isChecked: Boolean, id:Int){
        goalsDao.updateCheckedGoal(isChecked, id)
    }

    suspend fun insert(goals: Goals){
        goalsDao.insert(goals)
    }

    suspend fun delete(goals: Goals){
        goalsDao.delete(goals)
    }
}