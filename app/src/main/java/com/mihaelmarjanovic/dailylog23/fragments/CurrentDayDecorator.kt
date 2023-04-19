package com.mihaelmarjanovic.dailylog23.fragments

import android.R
import android.app.Activity
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mihaelmarjanovic.dailylog23.models.Day
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CurrentDayDecorator(context: Activity?, currentDay: CalendarDay, day: Day) : DayViewDecorator {
    private val drawable: Drawable?
    var myDay = currentDay
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
    }

    init {
        // You can set background for Decorator via drawable here
        if(day.rating!!.compareTo(5) == 0){
            drawable = ContextCompat.getDrawable(context!!, R.color.holo_green_dark)
        }
        else if(day.rating!!.compareTo(4) == 0){
            drawable = ContextCompat.getDrawable(context!!, R.color.holo_green_light)
        }
        else if(day.rating!!.compareTo(3) == 0){
            drawable = ContextCompat.getDrawable(context!!, com.mihaelmarjanovic.dailylog23.R.color.yellow)
        }
        else if(day.rating!!.compareTo(2) == 0){
            drawable = ContextCompat.getDrawable(context!!, com.mihaelmarjanovic.dailylog23.R.color.orange)
        }
        else if(day.rating!!.compareTo(1) == 0){
            drawable = ContextCompat.getDrawable(context!!, com.mihaelmarjanovic.dailylog23.R.color.red)
        }
        else{
            drawable = ContextCompat.getDrawable(context!!, R.color.system_accent1_0)
        }
    }
}