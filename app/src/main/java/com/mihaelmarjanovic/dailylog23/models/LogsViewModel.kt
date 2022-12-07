package com.mihaelmarjanovic.dailylog23.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mihaelmarjanovic.dailylog23.database.LogsDao
import com.mihaelmarjanovic.dailylog23.database.LogsDatabase
import com.mihaelmarjanovic.dailylog23.database.LogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LogsViewModel(application: Application): AndroidViewModel(application) {

    private val repository: LogsRepository
    private var dao: LogsDao
    private var calendar = Calendar.getInstance()
    var currentDate: String
    var currentTime: String
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    val timeFormatter = SimpleDateFormat("HH:mm:ss")

    private val _logs = MutableLiveData<List<Logs>>()
    val logs: LiveData<List<Logs>>
        get() = _logs

    init {
        //calendar = Calendar.getInstance()
        dao = LogsDatabase.getDatabase(application).getLogsDao()
        repository = LogsRepository(dao)
        currentDate = formatter.format(Calendar.getInstance().time)
        currentTime = timeFormatter.format(Calendar.getInstance().time)
    }

    fun nextDate(){
        calendar.add(Calendar.DAY_OF_MONTH, +1)
        currentDate = formatter.format(this.calendar.timeInMillis)
    }

    fun prevDate(){
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        currentDate = formatter.format(this.calendar.timeInMillis)
    }

    fun initializeLogs(date: String){
        viewModelScope.launch {
            _logs.value = repository.getAllLogs(date)
        }
    }

    fun deleteLog(logs: Logs) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(logs)
    }

    fun insertLog(logs: Logs) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(logs)
    }

    fun updateLogs(logs: Logs) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(logs)
    }

}