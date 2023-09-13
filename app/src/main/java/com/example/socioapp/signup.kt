package com.example.socioapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.socioapp.responses.signup_response
import com.example.socioapp.retrofitinstances_interface.Retrofitinstance
import com.example.socioapp.bodies.signuppost
import com.example.socioapp.responses.tokenresponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val username = findViewById<EditText>(R.id.editTextUsername).text
        val email = findViewById<EditText>(R.id.editTextEmail).text
        val password1 = findViewById<EditText>(R.id.editTextPassword).text
        val password2 = findViewById<EditText>(R.id.editTextConfirmPassword).text


        val button = findViewById<Button>(R.id.buttonSignUp)
        button.setOnClickListener {
            postdata(
                username.toString(),
                email.toString(),
                password1.toString(),
                password2.toString()
            )
        }

    }

    private fun postdata(username: String, email: String, password1: String, password2: String) {

        Toast.makeText(this, username, Toast.LENGTH_LONG).show()

        val post = signuppost(username, email, password1, password2)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", post.username)
            .addFormDataPart("email", post.email)
            .addFormDataPart("pass1", post.pass1)
            .addFormDataPart("pass2", post.pass2)
            .build()

        Retrofitinstance.apiInterface.signingup(requestBody).enqueue(object : Callback<signup_response?> {
            override fun onResponse(
                call: Call<signup_response?>?,
                response: Response<signup_response?>?
            ) {

                getoken(post.username,post.pass1)
            }

            override fun onFailure(call: Call<signup_response?>?, t: Throwable?) {
                Log.d("error", "invalid")
            }
        })
    }

    private fun getoken(username: String, pass1: String) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", pass1)
            .build()
        Retrofitinstance.apiInterface.send(requestBody).enqueue(object : Callback<tokenresponse?> {
            override fun onResponse(
                call: Call<tokenresponse?>,
                response: Response<tokenresponse?>
            ) {
                val intent = Intent(this@signup,MainActivity::class.java)
                val token = response.body()?.token.toString()
                intent.putExtra("username",username)
                intent.putExtra("password",pass1)
                startActivity(intent)
            }

            override fun onFailure(call: Call<tokenresponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}