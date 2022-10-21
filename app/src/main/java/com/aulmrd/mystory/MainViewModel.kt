package com.aulmrd.mystory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.aulmrd.mystory.data.repository.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getToken(): LiveData<String> {
        return repository.getToken().asLiveData()
    }
}