package com.example.reachbuddy.Models

import com.example.reachbuddy.utils.Constants.Companion.DEFAULT_IMAGE_URL

/*
3.5.21
Adding friend request and friends(both are mutablelist) in the userprofile data class
to determine the friends of the user and to deal with sending recieving and accepting friend request
 */
data class UserProfile(
    var UserName:String? = "Default Name",
    var UserProfilePicLink:String?= DEFAULT_IMAGE_URL,
    var LikesCount: Int? = 0,
    var UserBio:String? = "Default Bio",
    var LikedBy: MutableList<String> = mutableListOf() ,   //this will contains the uid of the users who liked the picture
    var FriendsList: MutableList<String> = mutableListOf(),
    var FriendRequestList: MutableList<String> = mutableListOf()
)