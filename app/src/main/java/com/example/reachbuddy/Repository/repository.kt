package com.example.reachbuddy.Repository

import com.example.reachbuddy.Daos.FirebaseDao
import com.example.reachbuddy.Models.Users

class repository(
){

    suspend fun writeuserdata(users: Users)=FirebaseDao.writeuser(users)

}