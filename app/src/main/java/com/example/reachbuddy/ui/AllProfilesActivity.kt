package com.example.reachbuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reachbuddy.Adapters.ProfileRecyclerViewAdapter
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.databinding.ActivityAllProfilesBinding
import com.example.reachbuddy.utils.Topspacingdecoration

class AllProfilesActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllProfilesBinding
    lateinit var viewModel: ProfileViewModel
    lateinit var profileadapter:ProfileRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAllProfilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this).get(ProfileViewModel::class.java)
        initrecyclerview()
        viewModel.updaterecyclerviewlist(profileadapter)


    }
    /*
   This will initialise the recyclerView
    */
    private fun initrecyclerview()
    {
        binding.profilesrecyclerview.apply {
            layoutManager= LinearLayoutManager(this@AllProfilesActivity)
            profileadapter= ProfileRecyclerViewAdapter()
            val topspace= Topspacingdecoration(5)
            addItemDecoration(topspace)
            adapter= profileadapter
        }
    }
}
