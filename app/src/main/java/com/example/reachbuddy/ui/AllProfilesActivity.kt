package com.example.reachbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reachbuddy.Adapters.ProfileRecyclerViewAdapter
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.databinding.ActivityAllProfilesBinding
import com.example.reachbuddy.utils.Constants.Companion.EXTRA_NAME
import com.example.reachbuddy.utils.Topspacingdecoration

class AllProfilesActivity : AppCompatActivity(),ProfileRecyclerViewAdapter.onClicklistener {
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
            profileadapter= ProfileRecyclerViewAdapter(this@AllProfilesActivity)
            val topspace= Topspacingdecoration(5)
            addItemDecoration(topspace)
            adapter= profileadapter
        }
    }

    override fun Onclick(position: Int) {
       // Toast.makeText(this,"Click Working", Toast.LENGTH_SHORT).show()
        val intent=Intent(this,ProfileViewingActivity::class.java)
        intent.putExtra(EXTRA_NAME,profileadapter.getprofileatpostion(position).UserName.toString())
        startActivity(intent)

    }
}
