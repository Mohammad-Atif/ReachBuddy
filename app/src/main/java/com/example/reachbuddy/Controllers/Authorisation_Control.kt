package com.example.reachbuddy.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.example.reachbuddy.MainActivity
import com.example.reachbuddy.R
import com.example.reachbuddy.utils.Constants.Companion.RC_SIGN_IN
import com.example.reachbuddy.utils.Constants.Companion.USER_IMAGE_KEY
import com.example.reachbuddy.utils.Constants.Companion.USER_NAME_KEY
import com.example.reachbuddy.utils.Constants.Companion.USER_UID_KEY


import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/*
Making this class to handle the google signIn functionality
Taking activity in constructor because google signin client requires googleSignInClient = GoogleSignIn.getClient(activity, gso)
i.e requires activity as a paramenter
Copyied the code from firebase authenticatin documnetation

 */
class Authorisation_Control(val activity: Activity){
    // Configure Google Sign In


// ...
// Initialize Firebase Auth
    val  auth = Firebase.auth
    val TAG = "GoogleActivity"




    fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken((R.string.default_web_client_id).toString())
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(activity,signInIntent, RC_SIGN_IN,null)        //this will launch google signin poppup
    }


    /*
    After a user successfully signs in, get an ID token from the GoogleSignInAccount object,
    exchange it for a Firebase credential, and authenticate with Firebase using the Firebase credential
     */
    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    get_pass_data_to_actiivty(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    get_pass_data_to_actiivty(null)
                }
            }
    }


    private fun get_pass_data_to_actiivty(user:FirebaseUser?)
    {
        if(user!=null)
        {
            val user_name=user.displayName
            val user_image=user.photoUrl.toString()
            val user_uid=user.uid
            val intent=Intent(activity,MainActivity::class.java)
            val bundle=Bundle()
            bundle.putString(USER_NAME_KEY,user_name)
            bundle.putString(USER_IMAGE_KEY,user_image)
            bundle.putString(USER_UID_KEY,user_uid)
            intent.putExtras(bundle)
            startActivity(activity.applicationContext,intent,null)

        }
    }


}