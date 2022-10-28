package com.aulmrd.mystory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.aulmrd.mystory.databinding.ActivityMainBinding
import com.aulmrd.mystory.ui.factory.FactoryUserViewModel
import com.aulmrd.mystory.ui.home.Home
import com.aulmrd.mystory.ui.login.LoginActivity
import com.aulmrd.mystory.ui.register.RegisterActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
<<<<<<< HEAD
    private lateinit var viewModel: MainViewModel
=======
>>>>>>> 3bea60da91d362780c4f78fe89e56a33802f5273

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factoryUserViewModel: FactoryUserViewModel = FactoryUserViewModel.getInstance(this)
        viewModel = ViewModelProvider(this, factoryUserViewModel) [MainViewModel::class.java]

        setupView()
        playAnimation()

        binding.btnMasuk1.setOnClickListener(this)
        binding.btnMasuk2.setOnClickListener(this)
<<<<<<< HEAD

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            viewModel.getToken().observe(this@MainActivity) {
                if (it != null)
                    Intent(this@MainActivity, Home::class.java).apply {
                        startActivity(this)
                    }
            }
        }
=======
>>>>>>> 3bea60da91d362780c4f78fe89e56a33802f5273

    }

    private fun setupView() {
        @Suppress ("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_masuk1 ->{
               val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_masuk2 ->{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgPeople, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnMasuk1, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnMasuk2, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tittle1, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tittle2, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply{
            playTogether(login, register)
        }
        AnimatorSet().apply {
            playTogether(title, desc, together)
            start()
        }
    }


}