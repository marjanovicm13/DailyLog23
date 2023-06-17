package com.mihaelmarjanovic.dailylog23.models

import android.app.Application
import androidx.lifecycle.*
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
    var calendar = Calendar.getInstance()
    var currentDate: String
    var currentDay: Int
    var currentMonth: Int
    var currentYear: Int
    var currentTime: String
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    val timeFormatter = SimpleDateFormat("HH:mm:ss")

    private val _currentDate = MutableLiveData<String>("")
    val currentDateIs: LiveData<String> get() = _currentDate

    private val _logs = MutableLiveData<List<Logs>>()
    val logs: LiveData<List<Logs>>
        get() = _logs

    init{
        dao = LogsDatabase.getDatabase(application).getLogsDao()
        repository = LogsRepository(dao)
        currentDate = formatter.format(Calendar.getInstance().time)
        currentTime = timeFormatter.format(Calendar.getInstance().time)
        currentDay = this.calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = this.calendar.get(Calendar.MONTH)
        currentYear = this.calendar.get(Calendar.YEAR)
        _currentDate.value = currentDate
    }

    fun nextDate(){
        calendar.add(Calendar.DAY_OF_MONTH, +1)
        currentDate = formatter.format(this.calendar.timeInMillis)
        currentDay = this.calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = this.calendar.get(Calendar.MONTH)
        currentYear = this.calendar.get(Calendar.YEAR)
        _currentDate.value = currentDate
    }

    fun prevDate(){
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        currentDate = formatter.format(this.calendar.timeInMillis)
        currentDay = this.calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = this.calendar.get(Calendar.MONTH)
        currentYear = this.calendar.get(Calendar.YEAR)
        _currentDate.value = currentDate
    }

    fun setDate(year: Int, month: Int, day: Int){
        currentDay = this.calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = this.calendar.get(Calendar.MONTH)
        currentYear = this.calendar.get(Calendar.YEAR)
        this.calendar.set(year, month, day)
        currentDate = formatter.format(this.calendar.timeInMillis)
        _currentDate.value = currentDate
    }

    fun getDate(): String{
        return _currentDate.value!!
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