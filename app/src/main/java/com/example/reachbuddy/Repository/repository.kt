package com.example.reachbuddy.Repository

import com.example.reachbuddy.Daos.FirebaseDao
import com.example.reachbuddy.Models.Users

/*
This is the main repository which provides data to the viewmodel
i.e it contains all the methods to get data from remote api or database
here we are getting data only from firebase
 */
class repository(
){

    suspend fun writeuserdata(users: Users)=FirebaseDao.writeuser(users)

}