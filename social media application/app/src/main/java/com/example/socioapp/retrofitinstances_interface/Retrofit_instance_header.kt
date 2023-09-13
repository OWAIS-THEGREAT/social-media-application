package com.example.socioapp.retrofitinstances_interface

import com.example.socioapp.interceptor.intercepter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit_instance_header {

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createApiService(token: String): Api_interface {
        val interceptor = intercepter(token)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = retrofit.newBuilder()
            .client(client)
            .build()

        return retrofit.create(Api_interface::class.java)
    }

}