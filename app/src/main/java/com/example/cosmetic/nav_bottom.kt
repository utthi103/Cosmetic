package com.example.cosmetic

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_bottom)
        view_pager = findViewById(R.id.view_pager)
        view_pager.adapter = viewPagerAdapter(supportFragmentManager)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.nav_bottom)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    view_pager.currentItem = 0
                    bottomNavigation.menu.findItem(R.id.home).isChecked = true // setChecked instead of setChecked(true)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.cart -> {
                    Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show()
                    view_pager.currentItem = 1
                    bottomNavigation.menu.findItem(R.id.cart).isChecked = true // setChecked instead of setChecked(true)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.account -> {
                    Toast.makeText(this, "Tài khoản", Toast.LENGTH_SHORT).show()
                    view_pager.currentItem = 2
                    bottomNavigation.menu.findItem(R.id.account).isChecked = true // setChecked instead of setChecked(true)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    view_pager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }

        nav_bottom = findViewById(R.id.nav_bottom) // initialize nav_bottom with the correct ID
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavigation.menu.findItem(R.id.home).isChecked = true // setChecked instead of setChecked(true)
                    }
                    1 -> {
                        bottomNavigation.menu.findItem(R.id.cart).isChecked = true // setChecked instead of setChecked(true)
                    }
                    2 -> {
                        bottomNavigation.menu.findItem(R.id.account).isChecked = true // setChecked instead of setChecked(true)
                    }
                    else -> {
                        view_pager.currentItem = 0
                    }
                }
            }
        })
    }}