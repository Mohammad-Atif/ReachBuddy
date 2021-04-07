package com.example.reachbuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reachbuddy.Models.UserProfile
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
        viewModel.getcurrentuserprofile()
        binding.TxtProfileBio.isEnabled=false

        viewModel.userprofile.observe(this, Observer {userprofile->
            binding.TxtProfileName.text=userprofile.UserName.toString()
            binding.TxtLikesCount.text=userprofile.LikesCount.toString()
            binding.TxtProfileBio.isEnabled=true
            binding.TxtProfileBio.setText(userprofile.UserBio.toString())
            binding.TxtProfileBio.isEnabled=false
            binding.TxtLikesCount.text=userprofile.LikesCount.toString()

        })



        binding.ImgEditBio.setOnClickListener {
            if(binding.TxtProfileBio.isEnabled==true)
            {
                viewModel.writeuserprofile(binding.TxtProfileBio.text.toString(),binding.TxtLikesCount.text.toString())
                binding.TxtProfileBio.isEnabled=false
            }
            else
            {
                binding.TxtProfileBio.isEnabled=true
                binding.TxtProfileBio.requestFocus()
            }
        }


    }
}
