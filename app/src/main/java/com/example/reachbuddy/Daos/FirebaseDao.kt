package com.example.reachbuddy.Daos

import android.util.Log
import com.example.reachbuddy.Models.UserMessage
import com.example.reachbuddy.Models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

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

        suspend fun WriteMsgtoDB(userMessage: UserMessage)
        {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH).toString()
            val day = c.get(Calendar.DAY_OF_MONTH).toString()

            val hour = c.get(Calendar.HOUR_OF_DAY).toString()
            val minute = c.get(Calendar.MINUTE).toString()
            val msgref=db.collection("MESSAGES")
            msgref.document("$month:$day:$hour:$minute").set(userMessage)
        }

        suspend fun getusersmesseges(){
            TODO()
        }


    }
}