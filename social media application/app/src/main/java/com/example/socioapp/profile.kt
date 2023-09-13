package com.example.socioapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import com.example.socioapp.retrofitinstances_interface.Retrofit_instance_header
import com.example.socioapp.responses.profile_response
import com.example.socioapp.responses.tokenresponse
import com.example.socioapp.retrofitinstances_interface.Retrofitinstance
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class profile : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val imag= findViewById<ImageView>(R.id.imageView)

        imag.setOnClickListener {
            openFilePicker()
        }
    }


    private fun setimage(img: Uri?) {

        val imageView: ImageView = findViewById(R.id.imageView)


        Picasso.get().load(img).into(imageView)


    }

    private fun postImage(imageUri: Uri) {
        val token = "189f667ac651847d55efd4a4e90e3f3a4ac57e26"
        val api_interface = Retrofit_instance_header.createApiService(token)

        val requestBodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("location", "pali")
            .addFormDataPart("bio", "I am owais the great")

        val imagePart = convertToImageFile(imageUri) // Convert selected image Uri to ImageFile format
        requestBodyBuilder.addPart(imagePart) // Attach the image to the request

        val requestBody = requestBodyBuilder.build()

        api_interface.profile(requestBody).enqueue(object : Callback<profile_response?> {
            override fun onResponse(
                call: Call<profile_response?>,
                response: Response<profile_response?>
            ) {
                val img = response.body()?.image.toString()
            }

            override fun onFailure(call: Call<profile_response?>, t: Throwable) {
                Log.d("err", "invalid credentials")
            }
        })
    }

    private fun convertToImageFile(imageUri: Uri): MultipartBody.Part {
        val imageStream = contentResolver.openInputStream(imageUri)
        val imageFile = File(cacheDir, "selected_image.jpg")
        val imageOutputStream = FileOutputStream(imageFile)

        imageStream?.use { input ->
            imageOutputStream.use { output ->
                input.copyTo(output)
            }
        }

        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
        return MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
    }




    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data

            val img = findViewById<ImageView>(R.id.imageView)
            img.setImageURI(selectedImageUri)
            if (selectedImageUri != null) {
                setimage(selectedImageUri)
//                val username = intent.getStringExtra("username").toString()
//                val password = intent.getStringExtra("password").toString()
                postImage(selectedImageUri)
            }

        }
    }

//    private fun gettoken(username: String, password: String,imageUri: Uri) {
//        val requestBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("username", username)
//            .addFormDataPart("password", password)
//            .build()
//        Retrofitinstance.apiInterface.send(requestBody).enqueue(object : Callback<tokenresponse?> {
//            override fun onResponse(
//                call: Call<tokenresponse?>,
//                response: Response<tokenresponse?>
//            ) {
////                response.body()?.let { postImage(imageUri, it.token) }
//                val texting = response.body()?.token
//                Log.d("res",texting.toString())
//                postImage(imageUri)
//            }
//
//            override fun onFailure(call: Call<tokenresponse?>, t: Throwable) {
//                Log.d("res","Invalid Credentials")
//            }
//        })
//    }
}