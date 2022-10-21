package com.aulmrd.mystory.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.aulmrd.mystory.data.result.Result
import com.aulmrd.mystory.MainActivity
import com.aulmrd.mystory.ui.register.RegisterActivity
import com.aulmrd.mystory.databinding.ActivityLoginBinding
import com.aulmrd.mystory.ui.factory.FactoryUserViewModel
import com.aulmrd.mystory.ui.home.Home

class LoginActivity : AppCompatActivity() {

        private lateinit var binding: ActivityLoginBinding
        private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        setupViewModel()
        playAnimation()
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(300)
        val title2 = ObjectAnimator.ofFloat(binding.tvLoginSubTitle, View.ALPHA, 1f).setDuration(300)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(300)
        val etPass = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(300)
        val title3 = ObjectAnimator.ofFloat(binding.tvHaventAccount, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, title2, etEmail, etPass, btnLogin, title3)
            start()
        }
    }

    private fun setupAction() {
        binding.tvHaventAccount.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        when{
            email.isEmpty() -> {
                binding.etEmail.error = "Enter your {email}"
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Enter your {password}"
            }
            else -> {
                loginViewModel.login(email, password).observe(this){ result ->
                   if (result != null){
                       when(result) {
                           is Result.Loading -> {
                               showLoading(true)
                           }
                           is Result.Success -> {
                               showLoading(false)
                               Toast.makeText(this, "login success! ${result.data.LoginResult?.token}", Toast.LENGTH_SHORT).show()
                               val user = result.data
                               val token = user.LoginResult?.token ?: ""
                               loginViewModel.setToken(token, doLogin = true)
                               loginViewModel.getToken().observe(this) { token ->
                                   if (token.isNotEmpty()) {
                                       startActivity(Intent(this, Home::class.java))
                                       finish()
                                   }
                               }
                           }
                           is Result.Error -> {
                               showLoading(false)
                               Toast.makeText(this, "Cannot Login, please check again!", Toast.LENGTH_SHORT).show()
                           }
                       }
                     }
                }
            }
        }
    }

    private fun setupViewModel(){
        val factoryUserViewModel: FactoryUserViewModel = FactoryUserViewModel.getInstance(this)
        loginViewModel = ViewModelProvider(this, factoryUserViewModel)[LoginViewModel::class.java]

    }

    private fun showLoading(doLoading: Boolean){
        binding.apply {
            etEmail.isEnabled = !doLoading
            etPassword.isEnabled = !doLoading
            btnLogin.isEnabled = !doLoading

            if(doLoading) {
                binding.progressBar.visibility = View.VISIBLE
            }else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}