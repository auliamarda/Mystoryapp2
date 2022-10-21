package com.aulmrd.mystory.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aulmrd.mystory.MainViewModel
import com.aulmrd.mystory.data.repository.UserRepository
import com.aulmrd.mystory.di.UserInject
import com.aulmrd.mystory.ui.login.LoginViewModel
import com.aulmrd.mystory.ui.register.RegisterViewModel
import java.lang.IllegalArgumentException

class FactoryUserViewModel(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var instance: FactoryUserViewModel? = null
        fun getInstance(context: Context): FactoryUserViewModel =
            instance ?: synchronized(this) {
                instance ?: FactoryUserViewModel(UserInject.provideRepository(context))
            }.also { instance = it }
    }
}