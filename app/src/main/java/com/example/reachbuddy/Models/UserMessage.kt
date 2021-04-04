package com.example.reachbuddy.Models

data class UserMessage(
    val user_message:String?,
    val user: Users =Users("default","default","default")
)