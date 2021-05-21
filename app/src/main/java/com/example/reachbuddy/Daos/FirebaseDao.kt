package com.example.reachbuddy.Daos

import android.util.Log
import com.example.reachbuddy.Models.UserMessage
import com.example.reachbuddy.Models.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
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

        /*
        This method is for when the user types message and press send button
        it will add the data in  the firestore database
         */
        suspend fun WriteMsgtoDB(userMessage: UserMessage)
        {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH).toString()
            val day = c.get(Calendar.DAY_OF_MONTH).toString()

            val hour = c.get(Calendar.HOUR_OF_DAY).toString()
            val minute = c.get(Calendar.MINUTE).toString()
            val sec = c.get(Calendar.SECOND).toString()
            val msgref=db.collection("MESSAGES")
            msgref.document("$month:$day:$hour:$minute:$sec").set(userMessage)
        }

        /*
        This function is called on the opening of app to get all the messeges from the firestore
        update -07.04.2021
        This function was not working because you cannot assign a value inside .addOnSu..Listnere
        more consicecy the messege mutable list is empty when we are returning it
        As soon as it leaves the curly braces the messege list values dissapeared and becomes what
        is used to be here- mutablelistof()..... means empty

         */
        suspend fun getusersmesseges():MutableList<UserMessage>{
            val messages: MutableList<UserMessage> = mutableListOf()
            val msgref=db.collection("MESSAGES")
            msgref.get().addOnSuccessListener { 
                lateinit var userMessage: UserMessage
                for(document in it)
                {
                    userMessage=document.toObject<UserMessage>()
                    messages.add(userMessage)
                }
            }
            Log.e("size","${messages.size}")
            return messages
        }

        /*
        20.5.21
        Now creating new getusermsg function which will return task and then we in the viewmodel
        apply onsucceslitner and perform our motive
         */



        suspend fun GetInstantMsg():MutableList<UserMessage>
        {
            val messages: MutableList<UserMessage> = mutableListOf()
            val msgref=db.collection("MESSAGES")
            msgref.addSnapshotListener{snapshot,e->
                if (e != null) {
                    Log.w("failed", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    lateinit var userMessage: UserMessage
                    for(document in snapshot)
                    {
                        userMessage=document.toObject<UserMessage>()
                        messages.add(userMessage)
                    }
                }

                }
            return messages
        }

    }


}
