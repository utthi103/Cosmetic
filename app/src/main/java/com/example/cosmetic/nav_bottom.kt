package com.example.cosmetic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.cosmetic.adapter.viewPagerAdapter
import com.example.cosmetic.db.countProduct
import com.google.android.material.bottomnavigation.BottomNavigationView


class nav_bottom : AppCompatActivity() {
    private var currentPage = 0
    private lateinit var view_pager: ViewPager
    private lateinit var nav_bottom: BottomNavigationView


    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_bottom)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val count = countProduct(this)
        val check_user = count.check_user()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.nav_bottom)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.view_pager, Fr_home.newInstance())
            addToBackStack(null)
            commit()
        }
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.view_pager, Fr_home.newInstance())
                        addToBackStack(null)
                        commit()
                    }
                    true
                }

                R.id.cart -> {
                    if(check_user){
                        Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show()
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.view_pager, Fr_cart.newInstance())
                            addToBackStack(null)
                            commit()
                        }

                    }else{
                        thongbao("Please login")
                    }

                    true
                }
                R.id.account -> {
                    if(check_user){
                        Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.view_pager, Fr_user.newInstance())
                            addToBackStack(null)
                            commit()
                        }
                    }else{
                        thongbao("Please login")
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
    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
           val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Xử lý sự kiện khi người dùng nhấn nút "Hủy bỏ"
        }
        builder.show()
    }
}
