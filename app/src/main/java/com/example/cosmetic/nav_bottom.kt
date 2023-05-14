package com.example.cosmetic

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.cosmetic.adapter.viewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


class nav_bottom : AppCompatActivity() {
    private var currentPage = 0
    private lateinit var view_pager: ViewPager
    private lateinit var nav_bottom: BottomNavigationView


    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_bottom)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.nav_bottom)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.view_pager, Fr_home.newInstance())
            addToBackStack(null)
            commit()
        }
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.view_pager, Fr_home.newInstance())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                R.id.cart -> {
                    Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.view_pager, Fr_cart.newInstance())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                R.id.account -> {
                    Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.view_pager, Fr_user.newInstance())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
                else -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.view_pager, Fr_home.newInstance())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }
            }
        }

        nav_bottom = findViewById(R.id.nav_bottom)
    }
}
