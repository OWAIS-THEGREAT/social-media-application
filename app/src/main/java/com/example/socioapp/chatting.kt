package com.example.socioapp

import BotResponseAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socioapp.bodies.Message
import com.example.socioapp.responses.botreponse
import com.example.socioapp.responses.rasaresponse
import com.example.socioapp.retrofitinstances_interface.Api_interface
import com.example.socioapp.retrofitinstances_interface.Retrofit_instance_header
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChattingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)


        val token = intent.getStringExtra("key")
        val but = findViewById<Button>(R.id.sendbut)
        val text = findViewById<EditText>(R.id.chattext).text
        but.setOnClickListener {
            if(text.isEmpty()){
                Toast.makeText(this,"enter the text",Toast.LENGTH_LONG).show()
            }
            else{
                gettext(text.toString(),token.toString())
                text.clear()
            }
        }

        getchat(token.toString())

    }

    private fun setadapter(botResponseList: ArrayList<String>) {
        val recyclerView: RecyclerView = findViewById(R.id.chatrecyclerview)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = BotResponseAdapter(botResponseList) // Replace with your list of BotResponseItems
        recyclerView.adapter = adapter

    }


    private fun makelist(chatlist: botreponse){
        val newList = ArrayList<String>()

        for (item in chatlist) {
            newList.add(item.sender)
            newList.add(item.botres)
        }

        setadapter(newList)
    }


    private fun getchat(token: String){
        val api_interface = Retrofit_instance_header.createApiService(token)


        api_interface.getchats().enqueue(object : Callback<botreponse?> {
            override fun onResponse(
                call: Call<botreponse?>,
                response: retrofit2.Response<botreponse?>
            ) {
                Log.d("res",response.body().toString())
                response.body()?.let { makelist(it) }
            }

            override fun onFailure(call: Call<botreponse?>, t: Throwable) {
                Log.d("err","Invalid Credentials")
            }
        })

    }

    private fun chat(token: String, text: String, extractedText: String) {


        val api_interface = Retrofit_instance_header.createApiService(token.toString())

        val requestBodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("sender", text)
            .addFormDataPart("chatbot", extractedText)

        val requestBody = requestBodyBuilder.build()

        api_interface.chatting(requestBody).enqueue(object : Callback<botreponse?> {
            override fun onResponse(
                call: Call<botreponse?>,
                response: retrofit2.Response<botreponse?>
            ) {
                Log.d("res",response.body().toString())
                response.body()?.let { makelist(it) }

            }

            override fun onFailure(call: Call<botreponse?>, t: Throwable) {
               Log.d("err","Invalid Credentials")
            }
        })
    }

    private fun gettext(text: String, token: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5005/") // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val yourApiService = retrofit.create(Api_interface::class.java)

        val requestBody = rasaresponse(text)

        yourApiService.sendMessage(requestBody).enqueue(object : Callback<List<Message>?> {
            override fun onResponse(
                call: Call<List<Message>?>,
                response: retrofit2.Response<List<Message>?>
            ) {
                if (response.isSuccessful) {
                    val yourResponseList = response.body()

                    if (yourResponseList != null && yourResponseList.isNotEmpty()) {
                        // Since there's only one object in the JSON array, you can directly access it
                        val firstResponse = yourResponseList[0]
                        val extractedText = firstResponse.text

                        chat(token,text,extractedText)


                    } else {
                        // Handle the case when the JSON array is empty
                    }
                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<List<Message>?>, t: Throwable) {

            }
        })
    }


}
