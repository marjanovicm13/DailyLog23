package com.mihaelmarjanovic.dailylog23.fragments

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColor
import com.google.android.material.color.MaterialColors
import com.mihaelmarjanovic.dailylog23.models.Day
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

@SuppressLint("ResourceType")
class CurrentDayDecorator(context: Activity?, currentDay: CalendarDay, day: Day) : DayViewDecorator {
    var myDay = currentDay
    private val context: Context;
    private val dayIs = day

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(
            ContextCompat.getDrawable(
                context!!,
                com.mihaelmarjanovic.dailylog23.R.drawable.selector
            )!!
        );
        if(dayIs.rating!!.compareTo(5)==0){
            view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, com.mihaelmarjanovic.dailylog23.R.color.greener)))
        }else if (dayIs.rating!!.compareTo(4) == 0) {
            view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, com.mihaelmarjanovic.dailylog23.R.color.green)))
        } else if (dayIs.rating!!.compareTo(3) == 0) {
            view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, com.mihaelmarjanovic.dailylog23.R.color.yellow)))
        } else if (dayIs.rating!!.compareTo(2) == 0) {
            view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, com.mihaelmarjanovic.dailylog23.R.color.orange)))
        } else if (dayIs.rating!!.compareTo(1) == 0) {
            view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, com.mihaelmarjanovic.dailylog23.R.color.red)))
        }
    }

    init {
        this.context = context!!
    }
}