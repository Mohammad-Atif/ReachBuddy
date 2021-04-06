package com.example.reachbuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reachbuddy.ViewModels.MenuViewModel
import com.example.reachbuddy.ViewModels.SocialViewModel
import com.example.reachbuddy.databinding.ActivityProfileViewingBinding

class ProfileViewingActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileViewingBinding
    lateinit var viewModel: MenuViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileViewingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel=ViewModelProvider(this).get(MenuViewModel::class.java)

        //Glide library to load image from a url into a imageView
        Glide.with(this).load(viewModel.imageurl).circleCrop().into(binding.ImgProfilePic)
        binding.TxtProfileName.text=viewModel.getuserclasshere().user_name

        binding.TxtLikesCount.text="0"

    }
}
