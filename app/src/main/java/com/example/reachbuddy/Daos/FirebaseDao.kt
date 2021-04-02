package com.example.reachbuddy.Daos

import android.util.Log
import com.example.reachbuddy.Models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseDao {

    companion object{
        val db=Firebase.firestore
        suspend fun writeuser(users: Users)
        {
            val ref=db.collection("USER")
            ref.document("userinfo").set(users).addOnSuccessListener {
                Log.e("write_log","Succesfulll")
            }
                .addOnFailureListener {
                    Log.e("write_log",it.toString())
                }
        }


    }
}