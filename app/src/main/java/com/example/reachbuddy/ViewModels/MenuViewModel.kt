package com.example.reachbuddy.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.reachbuddy.Models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MenuViewModel : ViewModel(){

    val imageurl=getuserclasshere().user_image_url

    private fun getuserclasshere():Users
    {
        val user= Firebase.auth.currentUser
        val user_name=user.displayName
        val user_uid=user.uid
        val user_image=user.photoUrl.toString()
        return Users(user_name,user_uid,user_image)
    }
}