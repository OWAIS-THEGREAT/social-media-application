package com.example.socioapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.socioapp.retrofitinstances_interface.Retrofitinstance
import com.example.socioapp.responses.signinresponse
import com.example.socioapp.bodies.signincred
import com.example.socioapp.responses.tokenresponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.editTextUsername).text
        val password = findViewById<EditText>(R.id.editTextPassword).text

        val but = findViewById<Button>(R.id.buttonSignIn)
        val already = findViewById<TextView>(R.id.textViewSignUp)
        but.setOnClickListener {
            signin(username.toString(), password.toString())
        }
        already.setOnClickListener {
            val intent = Intent(this,signup::class.java)
            startActivity(intent)
        }
    }

    private fun signin(username: String, password: String) {

        val post = signincred(username, password)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", post.username)
            .addFormDataPart("password", post.password)
            .build()
        Retrofitinstance.apiInterface.signin(requestBody)
            .enqueue(object : Callback<signinresponse?> {
                override fun onResponse(
                    call: Call<signinresponse?>?,
                    response: Response<signinresponse?>?
                ) {
                    gettoken(post.username,post.password)
                }

                override fun onFailure(call: Call<signinresponse?>?, t: Throwable?) {
                    Log.d("err", "Invalid credentials")
                }
            })
    }

    private fun gettoken(username: String, password: String) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()

        Retrofitinstance.apiInterface.send(requestBody).enqueue(object : Callback<tokenresponse?> {
            override fun onResponse(
                call: Call<tokenresponse?>,
                response: Response<tokenresponse?>
            ) {
               val intent = Intent(this@MainActivity,feed::class.java)
                intent.putExtra("key", response.body()?.token)
                intent.putExtra("username",username)
                startActivity(intent)
            }

            override fun onFailure(call: Call<tokenresponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}