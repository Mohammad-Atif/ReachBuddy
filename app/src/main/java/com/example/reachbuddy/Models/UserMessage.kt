package com.example.reachbuddy.Models

import androidx.annotation.Keep

@Keep
data class UserMessage(
    val user_message:String?="default",
    val user: Users =Users("default","default","default"),
    val message_time:String?="default"
)