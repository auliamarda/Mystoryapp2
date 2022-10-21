package com.aulmrd.mystory.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.aulmrd.mystory.MainActivity
import com.aulmrd.mystory.R

class KeluarActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnMasuklagi : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keluar)

        btnMasuklagi = findViewById(R.id.btn_masuklagi)
        btnMasuklagi.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.btn_masuklagi -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}