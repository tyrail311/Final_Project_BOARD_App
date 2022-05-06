package com.example.final_project_board_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.splash_screen)
        supportActionBar?.hide()
        snowflake.animate().apply {
            duration = 3000 // 1 second
            rotationX(360f) // rotate 360 degrees on Y axis
        }.start()
        Handler(Looper.getMainLooper()).postDelayed({
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        }, 5000) // 2 seconds delay
    }
}