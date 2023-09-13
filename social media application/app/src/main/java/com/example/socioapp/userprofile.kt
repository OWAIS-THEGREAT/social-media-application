package com.example.socioapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.socioapp.responses.followresposnse
import com.example.socioapp.retrofitinstances_interface.Retrofit_instance_header
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class userprofile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userprofile)
        val username = intent.getStringExtra("username")
        val following = intent.getStringExtra("following")

        val token  = intent.getStringExtra("key")
        followcheck(token,username,following)
        val texting = findViewById<TextView>(R.id.userName)
        texting.text = following
//        Toast.makeText(this,username+" "+following,Toast.LENGTH_LONG).show()

        val but = findViewById<Button>(R.id.follow)

        but.setOnClickListener {
            follow(token,username,following)
        }
    }

    private fun followcheck(token: String?, username: String?, following: String?) {
        val api_interface = Retrofit_instance_header.createApiService(token.toString())

        val requestBodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("follower", username.toString())
            .addFormDataPart("user", following.toString())

        val requestBody = requestBodyBuilder.build()

        api_interface.check(requestBody).enqueue(object : Callback<followresposnse?> {
            override fun onResponse(
                call: Call<followresposnse?>,
                response: Response<followresposnse?>
            ) {
                Log.d("res",response.body().toString())
               dofun(response.body()?.message.toString())
//                Toast.makeText(this@userprofile,response.body()?.message.toString(),Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<followresposnse?>, t: Throwable) {
                Log.d("err","Invalid Credentials")
            }
        })
    }

    private fun dofun(resp: String) {
        val but = findViewById<Button>(R.id.follow)
        if(resp =="yes"){

            but.text = "follow"
        }
        else{
            but.text = "unfollow"
        }
    }

    private fun follow(token: String?, username: String?, following: String?) {

        val api_interface = Retrofit_instance_header.createApiService(token.toString())

        val requestBodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("follower", username.toString())
            .addFormDataPart("user", following.toString())

        val requestBody = requestBodyBuilder.build()

        api_interface.follow(requestBody).enqueue(object : Callback<followresposnse?> {
            override fun onResponse(
                call: Call<followresposnse?>,
                response: Response<followresposnse?>
            ) {
                Log.d("res",response.body().toString())
                followcheck(token,username,following)
            }

            override fun onFailure(call: Call<followresposnse?>, t: Throwable) {
                Log.d("err","Invalid Credentials")
            }
        })
    }
}