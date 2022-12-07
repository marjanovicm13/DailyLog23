package com.mihaelmarjanovic.dailylog23.database

import androidx.lifecycle.LiveData
import com.mihaelmarjanovic.dailylog23.models.Logs

class LogsRepository(private val logsDao: LogsDao) {

    suspend fun getAllLogs(date: String): List<Logs>{
        return logsDao.getAllNotes(date)
    }

    suspend fun insert(logs: Logs){
        logsDao.insert(logs)
    }

    suspend fun delete(logs: Logs){
        logsDao.delete(logs)
    }

    suspend fun update(logs: Logs){
        logsDao.update(logs.id, logs.title, logs.log)
    }

}