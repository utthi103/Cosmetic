package com.example.cosmetic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cosmetic.databinding.ActivityCartDesignBinding

class cart_design : AppCompatActivity() {
    private lateinit var binding: ActivityCartDesignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartDesignBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}