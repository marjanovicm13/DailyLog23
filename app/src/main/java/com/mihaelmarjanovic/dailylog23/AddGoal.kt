package com.mihaelmarjanovic.dailylog23

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mihaelmarjanovic.dailylog23.databinding.ActivityAddGoalBinding
import com.mihaelmarjanovic.dailylog23.models.Goals
import java.lang.Exception

class AddGoal : AppCompatActivity() {

    private lateinit var binding: ActivityAddGoalBinding
    private lateinit var goal: Goals
    private lateinit var oldGoal: Goals
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGoalBinding.inflate(layoutInflater)

        setContentView(binding.root)

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

                intent.putExtra("goal", goal)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else {
                Toast.makeText(this@AddGoal, "Please enter a goal.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.imageViewBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}