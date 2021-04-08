package com.example.reachbuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reachbuddy.databinding.ActivityAllProfilesBinding

class AllProfilesActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllProfilesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAllProfilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
