package com.example.cosmetic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cosmetic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        home
        binding.btnSkip.setOnClickListener {
            val intent  = Intent(this, nav_bottom::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            val intent  = Intent(this, insertsp::class.java)
            startActivity(intent)
        }
        binding.txtsignin.setOnClickListener {
            val intent  = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}