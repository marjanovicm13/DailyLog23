package com.mihaelmarjanovic.dailylog23.database

import androidx.room.*
import com.mihaelmarjanovic.dailylog23.models.Goals
import com.mihaelmarjanovic.dailylog23.models.Logs

@Dao
interface GoalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goals: Goals)

    @Delete
    suspend fun delete(goals: Goals)

    @Query("SELECT * FROM goals WHERE date = :date ORDER BY id ASC")
    suspend fun getAllGoals(date: String): List<Goals>

    @Query("UPDATE goals SET isChecked=:isChecked WHERE id=:id")
    suspend fun updateCheckedGoal(isChecked: Boolean, id:Int)
}