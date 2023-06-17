package com.mihaelmarjanovic.dailylog23

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mihaelmarjanovic.dailylog23.databinding.ActivityAddLogBinding
import com.mihaelmarjanovic.dailylog23.models.Logs
import kotlinx.android.synthetic.main.activity_add_log.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



class AddLog : AppCompatActivity() {

    var pickedPhoto: Uri? = null
    var pickedBitMap: Bitmap? = null
    var image: String = ""

    private val CAMERA_REQUEST_CODE = 2
    private lateinit var currentPhotoPath: String
    private lateinit var photoURI: Uri
    private lateinit var matrix: Matrix

    private lateinit var binding: ActivityAddLogBinding
    private lateinit var log: Logs
    private lateinit var oldLog: Logs
    var isUpdate = false
    private lateinit var title:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLogBinding.inflate(layoutInflater)

        setContentView(binding.root)


        try {
            oldLog = intent.getSerializableExtra("current_log") as Logs
            binding.etTitle.setText(oldLog.title)
            binding.etLog.setText(oldLog.log)
            if(oldLog.image.toString().isNotEmpty() || oldLog.image.toString() != "") {
                binding.ivLogImage.setImageBitmap(
                    MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        Uri.parse(oldLog.image)
                    )
                )
            }
            isUpdate = true
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding.imageViewCheck.setOnClickListener{
            if(binding.etTitle.text.toString() == ""){
                title = intent.getSerializableExtra("date").toString()
            }
            else {
                title = binding.etTitle.text.toString()
            }
            val logContent = binding.etLog.text.toString()
            val logDate = intent.getSerializableExtra("date")
            val logTime = intent.getSerializableExtra("time")

            if(isUpdate == true){
                image = intent.getSerializableExtra("image").toString()
            }
            if(pickedPhoto != null) {
                image = pickedPhoto.toString()
            }
            val intent = Intent()

            if(title.isNotEmpty() || logContent.isNotEmpty()){
                if(isUpdate == true){
                    log = Logs(
                        oldLog.id,
                        title,
                        logContent,
                        logDate as String,
                        logTime as String,
                        image
                    )
                }
                else{
                    log = Logs(
                        null,
                        title,
                        logContent,
                        logDate as String,
                        logTime as String,
                        image
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

        binding.btnAddImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.btnCamera.setOnClickListener{
            cameraCheckPermission()
        }
    }

    private fun cameraCheckPermission(){
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(
                object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                camera()
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRationalDialogForPermission()
                    }
                }
            ).onSameThread().check()
    }

    private fun camera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    println("Creating image file")
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    println(" Error occurred while creating the File, in catch")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.mihaelmarjanovic.dailylog23.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }

            }
        }
    }

    private fun showRationalDialogForPermission(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned of permissions required for this").setPositiveButton("GO TO SETTINGS"){
            _,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                catch(e: ActivityNotFoundException){
                    e.printStackTrace()
                }
        }
            .setNegativeButton("CANCEL"){dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            println("current photo path "+ currentPhotoPath)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            pickedPhoto = data.data
            contentResolver.takePersistableUriPermission(pickedPhoto!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
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
        else if(requestCode == 2 && resultCode == RESULT_OK){
            pickedPhoto = photoURI
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