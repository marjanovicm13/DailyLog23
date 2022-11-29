package com.mihaelmarjanovic.dailylog23.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.databinding.ActivityMainBinding
import com.mihaelmarjanovic.dailylog23.databinding.FragmentDayBinding
import java.text.SimpleDateFormat
import java.util.*

class DayFragment : Fragment() {

    //private lateinit var binding: FragmentDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val time = Calendar.getInstance().time
        //val formatter = SimpleDateFormat("dd-MM-yyyy")
        //val current = formatter.format(time)
        //val currentDate = binding.tvCurrentDate
        //currentDate.text = current
        return inflater.inflate(R.layout.fragment_day, container, false)
    }
}