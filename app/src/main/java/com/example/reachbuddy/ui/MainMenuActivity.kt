package com.example.reachbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.R
import com.example.reachbuddy.ViewModels.MenuViewModel
import com.example.reachbuddy.ViewModels.SocialViewModel
import com.example.reachbuddy.databinding.ActivityMainMenuBinding
import com.example.reachbuddy.utils.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var viewModel: MenuViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        viewModel=ViewModelProvider(this).get(MenuViewModel::class.java)

        Glide.with(this).load(viewModel.imageurl).circleCrop().into(binding.UserProfileIcon)

        binding.btnPublicChat.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.UserProfileIcon.setOnClickListener {
            startActivity(Intent(this,ProfileViewingActivity::class.java))
        }

    }


}
