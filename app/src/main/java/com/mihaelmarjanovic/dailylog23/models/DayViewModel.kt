package com.mihaelmarjanovic.dailylog23.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mihaelmarjanovic.dailylog23.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DayViewModel(application: Application): AndroidViewModel(application) {

    private val repository: DayRepository
    private var dao: DayDao
    var currentDate: String
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    var calendar = Calendar.getInstance()

    private val _currentDate = MutableLiveData<String>("")
    val currentDateIs: LiveData<String> get() = _currentDate

    private var _dayRating = MutableLiveData<Day>()
    val dayRating: LiveData<Day>
        get() = _dayRating

    private var _days = MutableLiveData<List<Day>>()
    val days: LiveData<List<Day>>
        get() = _days


    init {
        dao = LogsDatabase.getDatabase(application).getDayDao()
        repository = DayRepository(dao)
        currentDate = formatter.format(Calendar.getInstance().time)
    }

    fun nextDate(){
        calendar.add(Calendar.DAY_OF_MONTH, +1)
        currentDate = formatter.format(this.calendar.timeInMillis)
      //  currentDay = this.calendar.get(Calendar.DAY_OF_MONTH)
        //currentMonth = this.calendar.get(Calendar.MONTH)
       // currentYear = this.calendar.get(Calendar.YEAR)
        _currentDate.value = currentDate
    }

    fun prevDate(){
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        currentDate = formatter.format(this.calendar.timeInMillis)
     //   currentDay = this.calendar.get(Calendar.DAY_OF_MONTH)
      //  currentMonth = this.calendar.get(Calendar.MONTH)
      //  currentYear = this.calendar.get(Calendar.YEAR)
        _currentDate.value = currentDate
    }

    fun setDate(year: Int, month: Int, day: Int){
      //  currentDay = this.calendar.get(Calendar.DAY_OF_MONTH)
      //  currentMonth = this.calendar.get(Calendar.MONTH)
     //   currentYear = this.calendar.get(Calendar.YEAR)
        this.calendar.set(year, month, day)
        currentDate = formatter.format(this.calendar.timeInMillis)
        _currentDate.value = currentDate
    }

    fun initializeDay(date: String){
        viewModelScope.launch {
            _dayRating.value = repository.getDayRating(date)
        }
    }

    fun initializeAllDays(){
        viewModelScope.launch {
            _days.value = repository.getAllDays()
        }
    }

    fun setNewRating(id: Int, newRating: Float){
        var updatedDay = Day(id, newRating, currentDate)
        _dayRating.value = updatedDay
    }

    fun getDate(): String{
        return _currentDate.value!!
    }

    fun insertDay(day: Day) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(day)
    }

    fun getDayRating(date: String) = viewModelScope.launch(Dispatchers.IO){
        repository.getDayRating(date)
    }

    fun deleteDay(day: Day) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(day)
    }

    fun updateDay() = viewModelScope.launch(Dispatchers.IO) {
        repository.update(_dayRating.value!!)
    }

}