package com.example.reachbuddy.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.reachbuddy.Models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/*
This is the viewModel used in MainMenu Activity and further(i created to used in MainMenu)

Here this ViewModel does not taking any values inside its constructor, so we does not have to
make its viewModelProvider factory class seperately.

It's instance is created just like  viewModel=ViewModelProvider(this).get(MenuViewModel::class.java)
Android will automatically provide its own viewModelProviderFactory

 */

class MenuViewModel : ViewModel(){

    val imageurl=getuserclasshere().user_image_url

    fun getuserclasshere():Users
    {
        val user= Firebase.auth.currentUser
        val user_name=user.displayName
        val user_uid=user.uid
        val user_image=user.photoUrl.toString()
        return Users(user_name,user_uid,user_image)
    }
}