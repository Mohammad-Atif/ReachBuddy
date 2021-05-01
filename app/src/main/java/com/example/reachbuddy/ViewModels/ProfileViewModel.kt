package com.example.reachbuddy.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reachbuddy.Adapters.ProfileRecyclerViewAdapter
import com.example.reachbuddy.Daos.ProfileDao
import com.example.reachbuddy.Models.UserProfile
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.utils.Constants.Companion.DEFAULT_BIO
import com.google.firebase.auth.ktx.auth
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

class ProfileViewModel : ViewModel(){

    val imageurl=getuserclasshere().user_image_url
    val LikedByUsers:MutableLiveData<MutableList<String>> = MutableLiveData()
    val isLikedByYou:MutableLiveData<Boolean> = MutableLiveData()
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

    fun writeuserprofile(newBio:String,likescount:String,list: MutableList<String>? = mutableListOf()){
        val userProfile= UserProfile(
            getuserclasshere().user_name,
            getuserclasshere().user_image_url,
            likescount.toInt(),
            newBio,
            list!!
        )

        viewModelScope.launch {
            dao.addUserProfile(userProfile)
        }
        userprofile.postValue(userProfile)
        LikedByUsers.postValue(userProfile.LikedBy)
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
                val e= userProfile!!.LikedBy
                LikedByUsers.postValue(e)
            }
            else
            {
                writeuserprofile(DEFAULT_BIO,"0")
            }
        }


        return userProfile
    }

    fun getisliked()
    {
        val task=dao.getProfileByName(getuserclasshere().user_name.toString())
        task.addOnSuccessListener {
            if (it.documents.size > 0)
            {
                val userprof = it.documents.get(0).toObject<UserProfile>()
                val listofLikedBy = userprof?.LikedBy
                var flag=0
                if (listofLikedBy != null) {
                    for (uid in listofLikedBy) {
                        if (uid == getuserclasshere().user_uid) {
                            isLikedByYou.postValue(true)
                            flag=1
                            break
                        }
                    }
                }
                if (flag == 0)
                    isLikedByYou.postValue(false)
             }
            else
                isLikedByYou.postValue(false)
        }
    }

    /*
    This functon will handle to increase and decrease the likes
     */

    fun managelikes()
    {
        val task=dao.getProfileByName(getuserclasshere().user_name.toString())
        task.addOnSuccessListener {
            val userprof=it.documents.get(0).toObject<UserProfile>()
            val listofLikedBy=userprof?.LikedBy
            var likesCount = userprof?.LikesCount!!.toInt()
            if(isLikedByYou.value==true)
            {
                listofLikedBy?.remove(getuserclasshere().user_uid.toString())
                likesCount = likesCount - 1
                isLikedByYou.postValue(false)
            }
            else
            {
                listofLikedBy?.add(getuserclasshere().user_uid.toString())
                likesCount = likesCount + 1
                isLikedByYou.postValue(true)
            }


            writeuserprofile(userprof?.UserBio.toString(),likesCount.toString(),listofLikedBy)
        }
    }

    fun updateBio(userBio:String)
    {
        val task=dao.getProfileByName(getuserclasshere().user_name.toString())
        task.addOnSuccessListener {
            val userprof=it.documents.get(0).toObject<UserProfile>()
            val listofLikedBy=userprof?.LikedBy
            val likesCount = userprof?.LikesCount!!.toInt()


            writeuserprofile(userBio,likesCount.toString(),listofLikedBy)
        }
    }

    fun updaterecyclerviewlist( profileadapter: ProfileRecyclerViewAdapter)
    {
        val task=dao.getAllUserProfiles()
        task.addOnSuccessListener {
            val list: MutableList<UserProfile> = mutableListOf()
            for(document in it)
            {
                val userProfile=document.toObject<UserProfile>()
                list.add(userProfile)
            }
            profileadapter.updatelist(list)
        }
    }




}