package com.mihaelmarjanovic.dailylog23.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.databinding.FragmentCalendarBinding
import com.mihaelmarjanovic.dailylog23.models.Day
import com.mihaelmarjanovic.dailylog23.models.DayViewModel
import com.mihaelmarjanovic.dailylog23.models.GoalsViewModel
import com.mihaelmarjanovic.dailylog23.models.LogsViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat


class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: LogsViewModel by activityViewModels()
    private val viewModelGoals: GoalsViewModel by activityViewModels()
    private val viewModelDays: DayViewModel by activityViewModels()
    private val formatter = SimpleDateFormat("dd-MM-yyyy")
    private lateinit var allDays: List<Day>

    private lateinit var mPreferences: SharedPreferences
    private val SP_THEME_KEY = "darkMode"
    private var themeIs: Boolean = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        mPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        if (mPreferences!!.contains(SP_THEME_KEY)) {
            themeIs = mPreferences.getBoolean(SP_THEME_KEY, false)
            if(themeIs == true){
                binding.materialCalendarView.setDateTextAppearance(R.color.white)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            viewModel.setDate(date.year, date.month, date.day)
            viewModelGoals.setDate(date.year, date.month, date.day)
        }

        runBlocking {
            viewModelDays.initializeAllDays()
            delay(50)
            allDays = viewModelDays.days.value!!
        }

        if(allDays != null) {
            for (day in allDays) {
                println(day)
                var myDate = CalendarDay.from(formatter.parse(day.date))
                if(day.rating!!.compareTo(0.0) == 0){}
                else {
                    binding.materialCalendarView.addDecorator(
                        CurrentDayDecorator(
                            activity,
                            myDate,
                            day
                        )
                    )
                }
            }
        }
        else{
            println("No days are rated.")
        }

        return binding.root
    }
}
