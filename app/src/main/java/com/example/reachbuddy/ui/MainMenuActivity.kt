package com.example.reachbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reachbuddy.Adapters.ProfileRecyclerViewAdapter
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.databinding.ActivityMainMenuBinding
import com.example.reachbuddy.utils.Constants.Companion.EXTRA_NAME

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        viewModel=ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.writeuserinfo()
        viewModel.getcurrentuserprofile(viewModel.getuserclasshere().user_name.toString())
        binding.imgrequests.visibility=View.INVISIBLE
        binding.requestcount.visibility=View.INVISIBLE
        viewModel.getRequestsCount()

        Glide.with(this).load(viewModel.imageurl).circleCrop().into(binding.UserProfileIcon)

        binding.btnPublicChat.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.UserProfileIcon.setOnClickListener {
            val intent=Intent(this,ProfileViewingActivity::class.java)
            intent.putExtra(EXTRA_NAME,viewModel.getuserclasshere().user_name.toString())
            startActivity(intent)
        }

        binding.BtnViewAllProfiles.setOnClickListener {
            startActivity(Intent(this,AllProfilesActivity::class.java))
        }

        viewModel.RequestsCount.observe(this, Observer { count->
            if(count>0)
            {
                binding.imgrequests.visibility=View.VISIBLE
                binding.requestcount.visibility=View.VISIBLE
                binding.requestcount.setText(count.toString())
            }
            else
            {
                binding.imgrequests.visibility=View.INVISIBLE
                binding.requestcount.visibility=View.INVISIBLE
            }
        })

        binding.imgrequests.setOnClickListener {
            startActivity(Intent(this,FriendRequestActivity::class.java))
        }




    }


}
