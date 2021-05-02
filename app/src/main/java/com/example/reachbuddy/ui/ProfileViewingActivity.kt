package com.example.reachbuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reachbuddy.R
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.databinding.ActivityProfileViewingBinding
import com.example.reachbuddy.utils.Constants.Companion.EXTRA_NAME

class ProfileViewingActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileViewingBinding
    lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileViewingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username=intent.getStringExtra(EXTRA_NAME).toString()

        viewModel=ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.getisliked(username)

        viewModel.getcurrentuserprofile(username)

        viewModel.imagelink.observe(this, Observer {link->
            //Glide library to load image from a url into a imageView
            Glide.with(this).load(link).circleCrop().into(binding.ImgProfilePic)
        })


        binding.TxtProfileBio.isEnabled=false

        viewModel.userprofile.observe(this, Observer {userprofile->
            binding.TxtProfileName.text=userprofile.UserName.toString()
            binding.TxtLikesCount.text=userprofile.LikesCount.toString()
            binding.TxtProfileBio.isEnabled=true
            binding.TxtProfileBio.setText(userprofile.UserBio.toString())
            binding.TxtProfileBio.isEnabled=false
            binding.TxtLikesCount.text=userprofile.LikesCount.toString()

        })

        viewModel.isLikedByYou.observe(this, Observer {
            if(it==true)
                binding.ImgLike.setImageResource(R.drawable.ic_favorite_black_24dp)
            else
                binding.ImgLike.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        })



        binding.ImgEditBio.setOnClickListener {
            if(username==viewModel.getuserclasshere().user_name.toString())
            {
                if(binding.TxtProfileBio.isEnabled==true)
                {
                    viewModel.updateBio(binding.TxtProfileBio.text.toString())
                    binding.TxtProfileBio.isEnabled=false
                }
                else
                {
                    binding.TxtProfileBio.isEnabled=true
                    binding.TxtProfileBio.requestFocus()
                }
            }

        }

        binding.ImgLike.setOnClickListener {
            viewModel.managelikes(username)
        }



    }
}
