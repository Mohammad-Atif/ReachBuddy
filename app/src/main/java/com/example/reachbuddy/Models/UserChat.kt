package com.example.reachbuddy.Models

data class UserChat(
    val messeges: MutableList<UserMessage> = mutableListOf()
)