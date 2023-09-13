package com.example.socioapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socioapp.responses.feed_response
import com.example.socioapp.retrofitinstances_interface.Retrofit_instance_header
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class feed : AppCompatActivity() {
    var recyclerView : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val but = findViewById<Button>(R.id.post)
        val token =  intent.getStringExtra("key")

        but.setOnClickListener {
            val intent = Intent(this,post::class.java)
            intent.putExtra("key",token)
//            intent.putExtra("username",username)
            startActivity(intent)
        }

        val chatbut = findViewById<Button>(R.id.chatbot)

        chatbut.setOnClickListener {
            val intent = Intent(this,ChattingActivity::class.java)
            intent.putExtra("key",token)
            startActivity(intent)
        }


        getfeed("")
    }

    private fun handleback(datalist : feed_response) {
        val username = intent.getStringExtra("username")
        val token = intent.getStringExtra("key")
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = MyAdapter(this,datalist,username.toString(),token.toString())

    }

    private fun getfeed(tok: String) {
        val token =  intent.getStringExtra("key")
        val api_interface = token?.let { Retrofit_instance_header.createApiService(it) }


        if (api_interface != null) {
            api_interface.feed().enqueue(object : Callback<feed_response?> {
                override fun onResponse(
                    call: Call<feed_response?>,
                    response: Response<feed_response?>
                ) {
                    Log.d("res",response.body().toString())
                    response.body()?.let { handleback(it) }
                }

                override fun onFailure(call: Call<feed_response?>, t: Throwable) {
                    Log.d("err","Invalid Credentials")
                }
            })
        }
    }
}