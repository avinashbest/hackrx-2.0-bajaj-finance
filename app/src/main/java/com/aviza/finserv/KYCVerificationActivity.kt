package com.aviza.finserv

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aviza.docscanner.ImageCropActivity
import com.aviza.docscanner.helpers.ScannerConstants
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_kycverification.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class KYCVerificationActivity : AppCompatActivity() {

    private lateinit var mCurrentPhotoPath: String
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kycverification)
        askPermission()
    }

    private fun askPermission() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 100
            )
        }
        setView()
    }

    private fun setView() {
        galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1111)
        }

        cameraButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null) {
                try {
                    photoFile = createImageFile()
                } catch (e: IOException) {
                    Log.i("Main", "IOException")
                }
                if (photoFile != null) {
                    val builder = StrictMode.VmPolicy.Builder()
                    StrictMode.setVmPolicy(builder.build())
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                    startActivityForResult(cameraIntent, 1231)
                }
            }
        }

        doneButton.setOnClickListener {
            if (TextUtils.isEmpty(firstNameInput.text.toString()) || TextUtils.isEmpty(lastNameInput.text.toString()) || TextUtils.isEmpty(
                    aadharNumberInput.text.toString()
                ) || TextUtils.isEmpty(phoneNumberInput.text.toString()) || TextUtils.isEmpty(
                    addressInput.text.toString()
                )
            ) {
                Toast.makeText(this, "Credentials can't be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            uploadingBar.visibility = View.VISIBLE
            uploadImage()
        }
    }

    private fun uploadImage() {
        val storageRef = Firebase.storage.reference
        val uniqueID = UUID.randomUUID().toString()
        val storageReference = storageRef.child(uniqueID)
        val stream = FileInputStream(photoFile)
        val uploadTask = storageReference.putStream(stream)

        uploadTask.addOnFailureListener {
            uploadingBar.visibility = View.GONE
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            Toast.makeText(this, "Upload Successful", Toast.LENGTH_LONG).show()
            uploadingBar.visibility = View.GONE
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1111 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            var bitmap: Bitmap? = null
            try {
                val inputStream = selectedImage?.let { contentResolver.openInputStream(it) }
                bitmap = BitmapFactory.decodeStream(inputStream)
                ScannerConstants.selectedImageBitmap = bitmap
                startActivityForResult(
                    Intent(this, ImageCropActivity::class.java),
                    1234
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == 1231 && resultCode == Activity.RESULT_OK) {
            ScannerConstants.selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                Uri.parse(mCurrentPhotoPath)
            )
            startActivityForResult(Intent(this, ImageCropActivity::class.java), 1234)
        } else if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            if (ScannerConstants.selectedImageBitmap != null) {
                imgBitmap.setImageBitmap(ScannerConstants.selectedImageBitmap)
            } else {
                Toast.makeText(this, "Not OK", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName, // prefix
            ".jpg", // suffix
            storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }
}