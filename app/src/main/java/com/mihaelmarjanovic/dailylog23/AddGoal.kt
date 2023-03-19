package com.mihaelmarjanovic.dailylog23

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mihaelmarjanovic.dailylog23.databinding.ActivityAddGoalBinding
import com.mihaelmarjanovic.dailylog23.models.Goals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*
import kotlin.math.min

class AddGoal : AppCompatActivity() {

    private lateinit var binding: ActivityAddGoalBinding
    private lateinit var goal: Goals
    private lateinit var oldGoal: Goals
    var isUpdate = false

    override fun onPause() {
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGoalBinding.inflate(layoutInflater)

        setContentView(binding.root)

        createNotificationChannel()

        try {
            oldGoal = intent.getSerializableExtra("current_goal") as Goals
            binding.etGoal.setText(oldGoal.goal)
            isUpdate = true
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding.imageViewCheck.setOnClickListener {
            val goalContent = binding.etGoal.text.toString()
            val goalDate = intent.getSerializableExtra("date")
            //val logTime = intent.getSerializableExtra("time")

            val intent = Intent()

            if (goalContent.isNotEmpty()) {
                if (isUpdate == true) {
                    goal = Goals(
                        oldGoal.id,
                        false,
                        goalContent,
                        goalDate as String
                    )
                } else {
                    goal = Goals(
                        null,
                        false,
                        goalContent,
                        goalDate as String
                    )
                }
                runBlocking {
                    scheduleNotification()
                }
                intent.putExtra("goal", goal)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else {
                Toast.makeText(this@AddGoal, "Please enter a goal.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val message = binding.etGoal.text.toString()
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
       // runBlocking {
            showAlert(time, message)
         //   delay(3000)
        //}
    }

    private fun showAlert(time: Long, message: String) {
        //Dont need this
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
        AlertDialog.Builder(this)
            .setTitle(titleExtra)
            .setMessage(
                "Message: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(time)
            )
            .setPositiveButton("Okay"){_,_ ->}
            .show()
            .dismiss()
    }

    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val currentDay = intent.getIntExtra("day", 0)
        val currentMonth = intent.getIntExtra("month", 0)
        val currentYear = intent.getIntExtra("year", 0)

        val calendar = Calendar.getInstance()
        calendar.set( currentYear, currentMonth, currentDay, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Goal notification channel"
            val desc = "Gives notification when to complete goals"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance)
            channel.description = desc
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}