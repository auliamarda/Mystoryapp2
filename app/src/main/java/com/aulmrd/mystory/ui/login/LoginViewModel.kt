package com.aulmrd.mystory.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aulmrd.mystory.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

fun setToken(token: String, doLogin: Boolean){
    viewModelScope.launch {
        repository.setToken(token, doLogin)
    }
}
    fun getToken(): LiveData<String>{
        return repository.getToken().asLiveData()
    }

    fun login(email: String, password: String) = repository.login(email, password)
}
