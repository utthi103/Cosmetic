package com.example.cosmetic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // delay picture
        Handler().postDelayed({
            startActivity(Intent(this,nav_bottom::class.java))
        },500)
    }
}