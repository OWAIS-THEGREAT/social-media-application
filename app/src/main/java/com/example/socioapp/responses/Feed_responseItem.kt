package com.example.socioapp.responses

data class feed_responseItem(
    val caption: String,
    val post_image_url: String,
    val user: String,
    val id:String,
    val likes:Int
)