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