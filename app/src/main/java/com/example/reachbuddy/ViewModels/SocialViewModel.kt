package com.example.reachbuddy.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reachbuddy.Daos.FirebaseDao
import com.example.reachbuddy.Models.UserChat
import com.example.reachbuddy.Models.UserMessage
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.Repository.repository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SocialViewModel(
    private val repository: repository
):ViewModel() {

    val userlist: MutableLiveData<User> = MutableLiveData()
    val messegelist : MutableLiveData<MutableList<UserMessage>> = MutableLiveData()
    val chats : MutableLiveData<MutableList<UserMessage>> = MutableLiveData()

    var piclink:String = "def"     //creating so that transefer data b/w fragments

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

    /*
    Funtion to handle send button click event on fragment private chat
     */

    fun writeChatmessage(msg: String){

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY).toString()
        val minute = c.get(Calendar.MINUTE).toString()
        val userMessage=UserMessage(msg,getuserclass(),"$hour:$minute")

        if(userMessage.user_message=="" || userMessage.user_message==" " || userMessage.user_message=="  ")
            return

        var l: MutableList<UserMessage>?= mutableListOf()
        if(!chats.value.isNullOrEmpty())
            l=chats.value
        l?.add(userMessage)
        chats.postValue(l)
        val uid1=getuserclass().user_uid.toString()
        val task=repository.getuid(piclink)
        task.addOnSuccessListener {
            val user=it.documents.get(0).toObject<Users>()
            val uid2= user?.user_uid.toString()
            viewModelScope.launch {
                repository.writePrivateMsg(l!!,uid1,uid2)
            }
        }

    }

    fun getInitalPrivateMsg(){
        val uid1=getuserclass().user_uid.toString()
        val taskforuid=repository.getuid(piclink)
        taskforuid.addOnSuccessListener {
            val user=it.documents.get(0).toObject<Users>()
            val uid2= user?.user_uid.toString()
            viewModelScope.launch {
                val task=repository.getPrivateMsg(uid1,uid2)
                task.addOnSuccessListener {chatdoc->
                    val c = chatdoc.toObject<UserChat>()
                    val messeges=c?.messeges
                    chats.postValue(messeges)


                }
            }
        }
    }

    fun realTimeMsg()
    {
        val uid1=getuserclass().user_uid.toString()
        val taskforuid=repository.getuid(piclink)
        taskforuid.addOnSuccessListener {
            val user=it.documents.get(0).toObject<Users>()
            val uid2= user?.user_uid.toString()
            val docref=repository.getdocref(uid1,uid2)
            docref.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("failed", "Listen failed.", error)
                    return@addSnapshotListener
                }

                if(value!=null)
                {
                    val userchat=value.toObject<UserChat>()
                    if (userchat != null) {
                        chats.postValue(userchat.messeges)
                    }
                }
            }
        }
    }




}