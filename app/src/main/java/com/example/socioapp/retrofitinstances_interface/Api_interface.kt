package com.example.socioapp.retrofitinstances_interface

import com.example.socioapp.*
import com.example.socioapp.bodies.Message
import com.example.socioapp.bodies.rasa_body
import com.example.socioapp.responses.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api_interface {

    @POST("signup/")
    fun signingup(
        @Body cred : MultipartBody
    ):Call<signup_response>

    @POST("api/token/")
    fun send(
        @Body token: MultipartBody
    ): Call<tokenresponse>

    @POST("signin/")
    fun signin(
        @Body cred : MultipartBody
    ):Call<signinresponse>

    @POST("setting/")
    fun profile(
        @Body cred : MultipartBody
    ):Call<profile_response>

    @POST("upload/")
    fun upload(
        @Body detail : MultipartBody
    ):Call<postresponse>

    @POST("feed/")
    fun feed():Call<feed_response>

    @POST("like/")
    fun like(
        @Body detail : MultipartBody
    ):Call<likeresponse>

    @POST("follow/")
    fun follow(
        @Body detail:MultipartBody
    ):Call<followresposnse>
    @POST("check/")
    fun check(
        @Body detail : MultipartBody
    ):Call<followresposnse>

    @POST("checklike/")
    fun checklike(
        @Body detail : MultipartBody
    ):Call<followresposnse>

    @POST("webhooks/rest/webhook") // Update with your actual Rasa endpoint
    fun sendMessage(
        @Body request: rasaresponse
    ): Call<List<Message>>

    @POST("chatting/")
    fun chatting(
        @Body detail:MultipartBody
    ):Call<botreponse>
    @POST("getchats/")
    fun getchats():Call<botreponse>
}