package com.example.cosmetic

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.cosmetic.Model.user
import com.example.cosmetic.databinding.ActivityMainBinding
import com.example.cosmetic.databinding.ActivityPaymentBinding
import com.google.firebase.database.*

class Payment : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var dpRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, Fr_cart::class.java)
            startActivity(intent)
        }

        setdata()
    }
    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thông báo")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Đồng ý") { dialog, which ->
            // Xử lý sự kiện khi người dùng nhấn nút "Đồng ý"
        }
//        builder.setNegativeButton("Hủy bỏ") { dialog, which ->
//            // Xử lý sự kiện khi người dùng nhấn nút "Hủy bỏ"
//        }
        builder.show()
    }

    private fun setdata() {
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser", null)
        dpRef = FirebaseDatabase.getInstance().getReference("user").child(iduser.toString())
        dpRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(user::class.java)
                    binding.etGmail.setText(user?.email.toString())
                    binding.etAddress.setText(user?.address.toString())
                    binding.etPhoneNumber.setText(user?.phone.toString())

                    binding.total.setText(  intent.getStringExtra("total"))
                }




            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
    }
}