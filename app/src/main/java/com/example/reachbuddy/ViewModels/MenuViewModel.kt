package com.example.reachbuddy.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reachbuddy.Daos.ProfileDao
import com.example.reachbuddy.Models.UserProfile
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.utils.Constants.Companion.DEFAULT_BIO
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


/*
This is the viewModel used in MainMenu Activity and further(i created to used in MainMenu)

Here this ViewModel does not taking any values inside its constructor, so we does not have to
make its viewModelProvider factory class seperately.

It's instance is created just like  viewModel=ViewModelProvider(this).get(MenuViewModel::class.java)
Android will automatically provide its own viewModelProviderFactory

 */

class MenuViewModel : ViewModel(){

    val imageurl=getuserclasshere().user_image_url

    val dao=ProfileDao()

    val userprofile:MutableLiveData<UserProfile> = MutableLiveData()

    fun getuserclasshere():Users
    {
        val user= Firebase.auth.currentUser
        val user_name=user.displayName
        val user_uid=user.uid
        val user_image=user.photoUrl.toString()
        return Users(user_name,user_uid,user_image)
    }

    fun getProfile()=dao.getUserProfileInstant(getuserclasshere().user_uid.toString())

    fun writeuserprofile(newBio:String,likescount:String){
        val userProfile= UserProfile(
            getuserclasshere().user_name,
            getuserclasshere().user_image_url,
            likescount.toInt(),
            newBio
        )

        viewModelScope.launch {
            dao.addUserProfile(userProfile)
        }
        userprofile.postValue(userProfile)
    }

    fun getcurrentuserprofile(): UserProfile?
    {
        var userProfile:UserProfile? = null

        val task=dao.getUserProfile(getuserclasshere().user_uid.toString())
        task.addOnSuccessListener {
            userProfile=it.toObject<UserProfile>()
            if(userProfile != null) {
                userprofile.postValue(userProfile)
                Log.e("ch","got executed")
            }
            else
            {
                writeuserprofile(DEFAULT_BIO,"0")
            }
        }


        return userProfile
    }
}