package com.example.reachbuddy.Daos

import android.util.Log
import com.example.reachbuddy.Models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/*
This is the Dao which contains all the methods of reading/writing from Firebase database
It provides all the methods only and it is used inside repository
 */

class FirebaseDao {

    companion object{
        val db=Firebase.firestore
        suspend fun writeuser(users: Users)
        {
            val ref=db.collection("USER")
            ref.document(users.user_uid.toString()).set(users).addOnSuccessListener {
                Log.e("write_log","Succesfulll")
            }
                .addOnFailureListener {
                    Log.e("write_log",it.toString())
                }
        }

        suspend fun getusersmesseges(){
            TODO()
        }


    }
}