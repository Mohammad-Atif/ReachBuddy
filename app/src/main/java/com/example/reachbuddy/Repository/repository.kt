package com.example.reachbuddy.Repository

import com.example.reachbuddy.Daos.FirebaseDao
import com.example.reachbuddy.Daos.ProfileDao
import com.example.reachbuddy.Models.UserMessage
import com.example.reachbuddy.Models.Users

/*
This is the main repository which provides data to the viewmodel
i.e it contains all the methods to get data from remote api or database
here we are getting data only from firebase
 */
class repository(
){
    private val profiledao=ProfileDao()

    suspend fun writeuserdata(users: Users)=FirebaseDao.writeuser(users)

    suspend fun getusermsgfirst()=FirebaseDao.getusersmesseges()

    suspend fun writePrivateMsg(userMessages: MutableList<UserMessage>,user1uid:String,user2uid:String) = FirebaseDao.writeInPrivateChat(userMessages,user1uid,user2uid)

    suspend fun getPrivateMsg(user1uid:String,user2uid:String)=FirebaseDao.getPrivatemsg(user1uid,user2uid)

    fun getuid(username:String)=profiledao.getuid(username)


}