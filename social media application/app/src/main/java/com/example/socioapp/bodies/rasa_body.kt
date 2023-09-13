package com.example.socioapp.bodies

data class rasa_body(val messages: List<Message>)

data class Message(val recipient_id: String, val text: String)