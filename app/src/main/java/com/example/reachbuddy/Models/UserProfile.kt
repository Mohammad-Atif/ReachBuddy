package com.example.reachbuddy.Models

import com.example.reachbuddy.utils.Constants.Companion.DEFAULT_IMAGE_URL

data class UserProfile(
    var UserName:String? = "Default Name",
    var UserProfilePicLink:String?= DEFAULT_IMAGE_URL,
    var LikesCount: Int? = 0,
    var UserBio:String? = "Default Bio",
    var LikedBy: MutableList<String> = mutableListOf()    //this will contains the uid of the users who liked the picture
)