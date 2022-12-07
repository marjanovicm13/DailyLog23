package com.mihaelmarjanovic.dailylog23

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mihaelmarjanovic.dailylog23.databinding.ActivityAddLogBinding
import com.mihaelmarjanovic.dailylog23.models.Logs
import java.lang.Exception
import java.util.*

class AddLog : AppCompatActivity() {

    private lateinit var binding: ActivityAddLogBinding
    private lateinit var log: Logs
    private lateinit var oldLog: Logs
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLogBinding.inflate(layoutInflater)

        setContentView(binding.root)

        try {
            oldLog = intent.getSerializableExtra("current_log") as Logs
            binding.etTitle.setText(oldLog.title)
            binding.etLog.setText(oldLog.log)
            isUpdate = true
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding.imageViewCheck.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val logContent = binding.etLog.text.toString()
            val logDate = intent.getSerializableExtra("date")
            val logTime = intent.getSerializableExtra("time")

            val intent = Intent()

            if(title.isNotEmpty() || logContent.isNotEmpty()){
                if(isUpdate == true){
                    log = Logs(
                        oldLog.id,
                        title,
                        logContent,
                        logDate as String,
                        logTime as String
                    )
                }
                else{
                    log = Logs(
                        null,
                        title,
                        logContent,
                        logDate as String,
                        logTime as String
                    )
                }
                intent.putExtra("log", log)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else{
                Toast.makeText(this@AddLog, "Please enter some data.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.imageViewBack.setOnClickListener{
            onBackPressed()
        }
    }
}