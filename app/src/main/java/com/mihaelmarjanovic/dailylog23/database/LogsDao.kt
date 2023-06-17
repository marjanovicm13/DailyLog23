package com.mihaelmarjanovic.dailylog23.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mihaelmarjanovic.dailylog23.models.Logs

@Dao
interface LogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(logs: Logs)

    @Delete
    suspend fun delete(logs: Logs)

    @Query("SELECT * FROM logs WHERE date = :date ORDER BY id ASC")
    suspend fun getAllLogs(date: String): List<Logs>

    @Query("UPDATE logs Set title = :title, log = :log, image = :image WHERE id = :id")
    suspend fun update(id: Int?, title: String?, log: String?, image: String?)

}