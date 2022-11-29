package com.mihaelmarjanovic.dailylog23.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.databinding.FragmentDayBinding
import java.text.SimpleDateFormat
import java.util.*


class DayFragment : Fragment() {

    private lateinit var binding: FragmentDayBinding
    private var calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(layoutInflater)
        setUI()
        return binding.root
    }

    private fun setUI(){
        //Setting current date
        var time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val current = formatter.format(time)
        val currentDate = binding.tvCurrentDate
        currentDate.text = current

        //Previous date
        binding.btnPrev.setOnClickListener{
            this.calendar.add(Calendar.DAY_OF_MONTH, -1)
            currentDate.text = formatter.format(this.calendar.timeInMillis)
        }
        //Next date
        binding.btnNext.setOnClickListener{
            this.calendar.add(Calendar.DAY_OF_MONTH, +1)
            currentDate.text = formatter.format(this.calendar.timeInMillis)
        }
    }


}