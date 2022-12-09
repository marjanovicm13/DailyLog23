package com.mihaelmarjanovic.dailylog23

import android.app.Activity
import android.content.Intent
import android.content.UriPermission
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mihaelmarjanovic.dailylog23.databinding.ActivityAddLogBinding
import com.mihaelmarjanovic.dailylog23.models.Logs
import kotlinx.android.synthetic.main.activity_add_log.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*

class AddLog : AppCompatActivity() {

    var pickedPhoto: Uri? = null
    var pickedBitMap: Bitmap? = null

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
            binding.ivLogImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver,Uri.parse(oldLog.image)))
            isUpdate = true
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding.imageViewCheck.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val logContent = binding.etLog.text.toString()
            val logDate = intent.getSerializableExtra("date")
            val logTime = intent.getSerializableExtra("time")
            val image = pickedPhoto

            val intent = Intent()

            if(title.isNotEmpty() || logContent.isNotEmpty()){
                if(isUpdate == true){
                    println("old image is " + oldLog.image)
                    log = Logs(
                        oldLog.id,
                        title,
                        logContent,
                        logDate as String,
                        logTime as String,
                        image.toString()
                    )
                }
                else{
                    log = Logs(
                        null,
                        title,
                        logContent,
                        logDate as String,
                        logTime as String,
                        pickedPhoto.toString()
                    )
                }
                println("new image is " + log.image)
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

        binding.btnAddImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            pickedPhoto = data.data
            println(pickedPhoto.toString())
            println(pickedPhoto)
            if(Build.VERSION.SDK_INT >= 28){
                val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
                pickedBitMap = ImageDecoder.decodeBitmap(source)
                ivLogImage.setImageBitmap(pickedBitMap)
            }
            else{
                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto)
                ivLogImage.setImageBitmap(pickedBitMap)
            }
        }
    }

}