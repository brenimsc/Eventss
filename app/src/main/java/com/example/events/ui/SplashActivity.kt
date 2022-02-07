package com.example.events.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.events.databinding.ActivitySplashBinding
import com.example.events.ui.extensions.animateView

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logo.animateView(400, 400)

        Handler().postDelayed(Runnable {
            kotlin.run {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }, 1000)
    }


}