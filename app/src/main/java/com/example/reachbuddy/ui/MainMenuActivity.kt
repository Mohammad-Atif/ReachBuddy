package com.example.reachbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        viewModel=ViewModelProvider(this).get(ProfileViewModel::class.java)

        Glide.with(this).load(viewModel.imageurl).circleCrop().into(binding.UserProfileIcon)

        binding.btnPublicChat.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.UserProfileIcon.setOnClickListener {
            startActivity(Intent(this,ProfileViewingActivity::class.java))
        }

        binding.BtnViewAllProfiles.setOnClickListener {
            startActivity(Intent(this,AllProfilesActivity::class.java))
        }

    }


}
