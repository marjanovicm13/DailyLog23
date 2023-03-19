package com.mihaelmarjanovic.dailylog23.fragments

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.mihaelmarjanovic.dailylog23.databinding.FragmentCalendarBinding
import com.mihaelmarjanovic.dailylog23.models.GoalsViewModel
import com.mihaelmarjanovic.dailylog23.models.LogsViewModel
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: LogsViewModel by activityViewModels()
    private val viewModelGoals: GoalsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(layoutInflater)

        binding.calendarView.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth -> // display the selected date by using a toast

            viewModel.setDate(year, month, dayOfMonth)
            viewModelGoals.setDate(year, month, dayOfMonth)

            binding.calendarView.setDate(viewModel.calendar.timeInMillis)
        })

        return binding.root
    }

}