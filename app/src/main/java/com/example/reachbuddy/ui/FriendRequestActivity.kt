package com.example.reachbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reachbuddy.Adapters.ProfileRecyclerViewAdapter
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.databinding.ActivityFriendRequestBinding
import com.example.reachbuddy.utils.Constants
import com.example.reachbuddy.utils.Topspacingdecoration

class FriendRequestActivity : AppCompatActivity(),ProfileRecyclerViewAdapter.onClicklistener {

    lateinit var binding: ActivityFriendRequestBinding
    lateinit var viewModel: ProfileViewModel
    lateinit var profileadapter:ProfileRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFriendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel= ViewModelProvider(this).get(ProfileViewModel::class.java)
        initrecyclerview()
        viewModel.upadateForFriendsRecyclerView(profileadapter)

    }

    /*
  This will initialise the recyclerView
   */
    private fun initrecyclerview()
    {
        binding.userprofilesrecyclerview.apply {
            layoutManager= LinearLayoutManager(this@FriendRequestActivity)
            profileadapter= ProfileRecyclerViewAdapter(this@FriendRequestActivity, Constants.FOR_REQUESTS_VIEWING)
            val topspace= Topspacingdecoration(5)
            addItemDecoration(topspace)
            adapter= profileadapter
        }
    }

    override fun Onclick(position: Int) {
        Toast.makeText(this,"Click Working", Toast.LENGTH_SHORT).show()



    }
}
