package com.example.reachbuddy.Daos

import android.util.Log
import com.example.reachbuddy.Models.UserProfile
import com.example.reachbuddy.Models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlin.math.log

/*
This is the dao which contains all the methods related to user profile adding or retrieving

 */
class ProfileDao {

    val db=Firebase.firestore
    val Usersref=db.collection("USER")
    val userProfileref=db.collection("PROFILES")


    suspend fun addUserProfile(userProfile: UserProfile){
        val User_uid=getuserclass().user_uid
        userProfileref.document(User_uid.toString()).set(userProfile).addOnFailureListener {
            Log.e("writing failed", it.toString())
        }
    }

    fun getUserProfileInstant(User_uid:String):UserProfile
    {
        lateinit var userProfile: UserProfile
        userProfileref.whereEqualTo("UserName",getuserclass().user_name.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("listen failed", "Listen failed.")
                    return@addSnapshotListener
                }
                if (value != null) {
                    for(document in value)
                    {
                        userProfile=document.toObject<UserProfile>()
                    }
                }
            }
        return userProfile
    }

    /*
    Returning task here because onSucceslistener is a lambda and you cant assign a value inside it and
    return it so it returns task
    Now in viewmodel we use .onSuccesslistener and inside it we update the viewModel.
     */

    fun getUserProfile(User_uid:String): Task<DocumentSnapshot>
    {

        val getting: Task<DocumentSnapshot> = userProfileref.document(User_uid).get()

        return getting
    }

    /*
    Return uid of persons who likes the profile of current user name\
    obviously returning task now read above comment for explanation :p
     */

    fun getProfileByName(userName:String): Task<QuerySnapshot>
    {
        return userProfileref.whereEqualTo("userName",userName).get()
    }

    fun getAllUserProfiles(): Task<QuerySnapshot>
    {
        return userProfileref.get()
    }



    private fun getuserclass(): Users
    {
        val user= Firebase.auth.currentUser
        val user_name=user.displayName
        val user_uid=user.uid
        val user_image=user.photoUrl.toString()
        return Users(user_name,user_uid,user_image)
    }
}