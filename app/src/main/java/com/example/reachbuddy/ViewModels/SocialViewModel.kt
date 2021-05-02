package com.example.reachbuddy.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reachbuddy.Daos.FirebaseDao
import com.example.reachbuddy.Models.UserMessage
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.Repository.repository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SocialViewModel(
    val repository: repository
):ViewModel() {

    val userlist: MutableLiveData<User> = MutableLiveData()
    val messegelist : MutableLiveData<MutableList<UserMessage>> = MutableLiveData()


    /*
    This function is to get the messeges in the realtime
    this will fetch the data from the repository in the realtime
     */
    fun getmessages(){
        viewModelScope.launch {

            val l=FirebaseDao.GetInstantMsg()
            messegelist.postValue(l)
        }
    }
    /*
    This method is get called when the users types message and press send button
    it will update the list
     */

    fun writemessage(userMessage: UserMessage){

        if(userMessage.user_message=="" || userMessage.user_message==" " || userMessage.user_message=="  ")
            return

        var l: MutableList<UserMessage>?= mutableListOf()
        if(!messegelist.value.isNullOrEmpty())
            l=messegelist.value
        l?.add(userMessage)
        messegelist.postValue(l)

        viewModelScope.launch {
            FirebaseDao.WriteMsgtoDB(userMessage)
        }
    }

    /*
    This is the function to get all the messeges when the app is opened
    update 07.04.2021
    This will not work For explanation visit FirebaseDao.kt
     */
    fun initialgetmsg():MutableList<UserMessage>
    {
        var list: MutableList<UserMessage> = mutableListOf()
        viewModelScope.launch {
            list=repository.getusermsgfirst()
        }
        messegelist.postValue(list)
        return list
    }

    fun getuserclass():Users
    {
        val user= Firebase.auth.currentUser
        val user_name=user.displayName
        val user_uid=user.uid
        val user_image=user.photoUrl.toString()
        return Users(user_name,user_uid,user_image)
    }



}