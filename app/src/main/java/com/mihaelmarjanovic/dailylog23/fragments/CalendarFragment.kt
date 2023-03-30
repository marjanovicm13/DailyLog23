package com.mihaelmarjanovic.dailylog23.fragments

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.databinding.FragmentCalendarBinding
import com.mihaelmarjanovic.dailylog23.models.Day
import com.mihaelmarjanovic.dailylog23.models.DayViewModel
import com.mihaelmarjanovic.dailylog23.models.GoalsViewModel
import com.mihaelmarjanovic.dailylog23.models.LogsViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: LogsViewModel by activityViewModels()
    private val viewModelGoals: GoalsViewModel by activityViewModels()
    private val viewModelDays: DayViewModel by activityViewModels()
    private val formatter = SimpleDateFormat("dd-MM-yyyy")
    private lateinit var allDays: List<Day>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(layoutInflater)
/*        binding.calendarView.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth -> // display the selected date by using a toast

            viewModel.setDate(year, month, dayOfMonth)
            viewModelGoals.setDate(year, month, dayOfMonth)

            binding.calendarView.setDate(viewModel.calendar.timeInMillis)
        })*/

       binding.materialCalendarView.setOnDateChangedListener(OnDateSelectedListener{ view, date, bool ->
            viewModel.setDate(date.year, date.month, date.day)
            viewModelGoals.setDate(date.year, date.month, date.day)
        })

        runBlocking {
            viewModelDays.initializeAllDays()
            delay(50)
            allDays = viewModelDays.days.value!!
        }

        if(allDays != null) {
            println("its not null")
            for (day in allDays) {
                println(day)
                var myDate = CalendarDay.from(formatter.parse(day.date))
                binding.materialCalendarView.addDecorator(CurrentDayDecorator(activity, myDate, day))
            }
        }
        else{
            println("its null")
        }
       // val myDate = CalendarDay.from(viewModel.currentYear, viewModel.currentMonth, viewModel.currentDay)


        return binding.root
    }
}
