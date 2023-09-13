package com.example.socioapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.socioapp.retrofitinstances_interface.Retrofit_instance_header
import com.example.socioapp.responses.postresponse
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class post : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val imag= findViewById<ImageView>(R.id.postimg)


        imag.setOnClickListener {
            openFilePicker()
        }

        val but = findViewById<Button>(R.id.done)
        val token = intent.getStringExtra("key")
        but.setOnClickListener {
            val intent = Intent(this,feed::class.java)
            intent.putExtra("key",token)
            startActivity(intent)
        }
    }


    private fun setimage(img: Uri?) {

        val imageView: ImageView = findViewById(R.id.postimg)


        Picasso.get().load(img).into(imageView)

    }

    private fun postImage(imageUri: Uri, caption: String) {
        val token = intent.getStringExtra("key").toString()
        val api_interface = Retrofit_instance_header.createApiService(token)

        val requestBodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("caption",caption )

        val imagePart = convertToImageFile(imageUri) // Convert selected image Uri to ImageFile format
        requestBodyBuilder.addPart(imagePart) // Attach the image to the request

        val requestBody = requestBodyBuilder.build()

        api_interface.upload(requestBody).enqueue(object : Callback<postresponse?> {
            override fun onResponse(call: Call<postresponse?>, response: Response<postresponse?>) {
                response.body()?.let { Log.d("res", it.username) }
            }

            override fun onFailure(call: Call<postresponse?>, t: Throwable) {
                Log.d("err","Invalid creadentials")
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

            val img = findViewById<ImageView>(R.id.postimg)
            img.setImageURI(selectedImageUri)
            val text = findViewById<EditText>(R.id.captioning).text
            if (selectedImageUri != null) {
                setimage(selectedImageUri)
                postImage(selectedImageUri,text.toString())
            }

        }
    }
}