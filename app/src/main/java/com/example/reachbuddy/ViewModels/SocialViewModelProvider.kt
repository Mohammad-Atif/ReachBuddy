package com.example.reachbuddy.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reachbuddy.Repository.repository

class SocialViewModelProvider(
    val repository: repository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SocialViewModel(repository) as T
    }
}