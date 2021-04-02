package com.example.reachbuddy.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.Repository.repository
import kotlinx.coroutines.launch

class SocialViewModel(
    val repository: repository
):ViewModel() {

    fun writeuserinfo(users: Users){

        viewModelScope.launch {
            repository.writeuserdata(users)
        }
    }
}