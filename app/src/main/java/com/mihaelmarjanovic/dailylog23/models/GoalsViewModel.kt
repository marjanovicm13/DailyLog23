package com.mihaelmarjanovic.dailylog23.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mihaelmarjanovic.dailylog23.database.GoalsDao
import com.mihaelmarjanovic.dailylog23.database.GoalsRepository
import com.mihaelmarjanovic.dailylog23.database.LogsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GoalsViewModel(application: Application): AndroidViewModel(application)  {
    private val goalsRepository: GoalsRepository
    private var dao: GoalsDao
    private var calendar = Calendar.getInstance()
    var currentDate: String
    var currentDay: Int
    var currentMonth: Int
    var currentYear: Int
    val formatter = SimpleDateFormat("dd-MM-yyyy")

    private val _currentDate = MutableLiveData<String>("")
    val currentDateIs: LiveData<String> get() = _currentDate

    private val _goals = MutableLiveData<List<Goals>>()
    val goals: LiveData<List<Goals>>
        get() = _goals

    init {
        dao = LogsDatabase.getDatabase(application).getGoalsDao()
        goalsRepository = GoalsRepository(dao)
        currentDate = formatter.format(Calendar.getInstance().time)
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

    fun initializeGoals(date: String){
        viewModelScope.launch {
            _goals.value = goalsRepository.getAllGoals(date)
        }
    }

    fun updateCheckedGoal(isChecked: Boolean, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        goalsRepository.updateCheckedGoal(isChecked, id)
    }

    fun deleteGoal(goals: Goals) = viewModelScope.launch(Dispatchers.IO) {
        goalsRepository.delete(goals)
    }

    fun insertGoal(goals: Goals) = viewModelScope.launch(Dispatchers.IO) {
        goalsRepository.insert(goals)
    }
}