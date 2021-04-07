package com.example.reachbuddy.Models

import com.example.reachbuddy.utils.Constants.Companion.DEFAULT_IMAGE_URL

data class UserProfile(
    var UserName:String? = "Default Name",
    var UserProfilePicLink:String?= DEFAULT_IMAGE_URL,
    var LikesCount: Int? = 0,
    var UserBio:String? = "Default Bio"
)