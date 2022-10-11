package com.aulmrd.mystory.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.aulmrd.mystory.data.result.Result
import com.aulmrd.mystory.databinding.ActivityRegisterBinding
import com.aulmrd.mystory.ui.factory.FactoryUserViewModel
import com.aulmrd.mystory.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        setupViewModel()
        playAnimation()
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(300)
        val title2 = ObjectAnimator.ofFloat(binding.tvRegisterSubTitle, View.ALPHA, 1f).setDuration(300)
        val etName = ObjectAnimator.ofFloat(binding.etFullname, View.ALPHA, 1f).setDuration(300)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(300)
        val etPass = ObjectAnimator.ofFloat(binding.etRegisterPassword, View.ALPHA, 1f).setDuration(300)
        val btnRegis = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)
        val title3 = ObjectAnimator.ofFloat(binding.tvHaveAccount, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, title2, etName, etEmail, etPass, btnRegis, title3)
            start()
        }
    }

    private fun setupViewModel(){
        val factoryUserViewModel: FactoryUserViewModel = FactoryUserViewModel.getInstance(this)
        registerViewModel = ViewModelProvider(this, factoryUserViewModel)[RegisterViewModel::class.java]
    }

    private fun setupAction(){
        binding.btnRegister.setOnClickListener{
            val name = binding.etFullname.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()
            when{
                name.isEmpty() -> {
                    binding.etFullname.error = "Enter your {name}"
                }
                email.isEmpty() -> {
                    binding.etEmail.error = "Enter your {email}"
                }
                password.isEmpty() -> {
                    binding.etRegisterPassword.error = "Enter your {password}"
                }
                else -> {
                    registerViewModel.register(name, email, password).observe(this){ result ->
                        if (result != null){
                            when(result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    val user = result.data
                                    if (user.error) {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            user.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        AlertDialog.Builder(this@RegisterActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage("Your account successfully created")
                                            setPositiveButton("Next") { _, _ ->
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        this,
                                        "Cannot Register, please check again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }


        binding.tvHaveAccount.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }



    private fun showLoading(doLoading: Boolean) {
        binding.apply {
            etFullname.isEnabled = !doLoading
            etEmail.isEnabled = !doLoading
            etRegisterPassword.isEnabled = !doLoading
            btnRegister.isEnabled = !doLoading

            if (doLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }
}