package com.example.reachbuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.reachbuddy.R
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.databinding.ActivityPrivateChatBinding
import com.example.reachbuddy.ui.Fragments.FriendsListFragment
import kotlinx.android.synthetic.main.activity_private_chat.view.*

class PrivateChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityPrivateChatBinding
    lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPrivateChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel= ViewModelProvider(this).get(ProfileViewModel::class.java)

        val friendslistfragment=FriendsListFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentholder,friendslistfragment)
            commit()
        }

    }
}
