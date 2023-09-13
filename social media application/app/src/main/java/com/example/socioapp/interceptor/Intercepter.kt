package com.example.socioapp.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class intercepter(private val token:String):Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()


        val modifiedRequest = request.newBuilder()
            .header("Authorization", "Token $token")
            .build()

        val response = chain.proceed(modifiedRequest)
        Log.d("Interceptor", "Request URL: ${request.url}")
        Log.d("Interceptor", "Response Code: ${response.code}")

        return response
    }
}