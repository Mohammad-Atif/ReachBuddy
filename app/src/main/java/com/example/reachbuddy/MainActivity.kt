package com.example.reachbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.Repository.repository
import com.example.reachbuddy.ViewModels.SocialViewModel
import com.example.reachbuddy.ViewModels.SocialViewModelProvider
import com.example.reachbuddy.utils.Constants
import com.example.reachbuddy.utils.Constants.Companion.USER_IMAGE_KEY
import com.example.reachbuddy.utils.Constants.Companion.USER_NAME_KEY
import com.example.reachbuddy.utils.Constants.Companion.USER_UID_KEY

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SocialViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

           //generating the user instance from the data from login activity
        val rep=repository()
        val viewmodelProvider=SocialViewModelProvider(rep)
        viewModel=ViewModelProvider(this,viewmodelProvider).get(SocialViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        val user=getuserclass()
        viewModel.writeuserinfo(user)
    }

    //function to get all the data from the login activity and creating and returning
    //the user instance from the data to be used in write function
    fun getuserclass():Users
    {
        val bundle=intent.extras
        val user_name=bundle?.getString(USER_NAME_KEY)
        val user_uid=bundle?.getString(USER_UID_KEY)
        val user_image=bundle?.getString(USER_IMAGE_KEY)
        return Users(user_name,user_uid,user_image)
    }
}
