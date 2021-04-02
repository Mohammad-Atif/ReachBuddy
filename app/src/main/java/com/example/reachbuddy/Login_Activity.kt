package com.example.reachbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
//import com.example.reachbuddy.Controllers.Authorisation_Control
import com.example.reachbuddy.utils.Constants
//import com.example.reachbuddy.utils.Constants.Companion.RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login_.*

class Login_Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
//    private lateinit var authcontroller:Authorisation_Control

    private lateinit var googleSignInClient:GoogleSignInClient


    companion object{
        private const val TAG="SIGIN_CHECK_TAG"
        private const val RC_SIGN_IN = 9001

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_)
        //authcontroller= Authorisation_Control(this)
        auth=Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_signIn.setOnClickListener {
            signIn()
        }


    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
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

    private fun get_pass_data_to_actiivty(user: FirebaseUser?)
    {
        if(user!=null)
        {
            val user_name=user.displayName
            val user_image=user.photoUrl.toString()
            val user_uid=user.uid
            val intent=Intent(this,MainActivity::class.java)
            val bundle=Bundle()
            bundle.putString(Constants.USER_NAME_KEY,user_name)
            bundle.putString(Constants.USER_IMAGE_KEY,user_image)
            bundle.putString(Constants.USER_UID_KEY,user_uid)
            intent.putExtras(bundle)
            startActivity(intent)

        }
    }

}
