package com.example.socioapp.retrofitinstances_interface

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retrofitobject_chatbot {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5005/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val apiInterface by lazy {
        retrofit.create(Api_interface::class.java)
    }
}