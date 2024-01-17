package com.example.cosmetic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cosmetic.Model.*
import com.example.cosmetic.adapter.cartAdapter
import com.example.cosmetic.databinding.ActivityMainBinding
import com.example.cosmetic.databinding.ActivityPaymentBinding
import com.example.cosmetic.db.countProduct
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Payment : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var dpRef1: DatabaseReference
    private lateinit var dpRef: DatabaseReference
    private lateinit var dpRef2: DatabaseReference
    private lateinit var dpRef3: DatabaseReference
    private lateinit var dpDaban: DatabaseReference
    private lateinit var dpDaban1: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, nav_bottom::class.java)
            startActivity(intent)
        }
            binding.btnPay.setOnClickListener {
                order()
            }
        setdata()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun order() {
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences.getString("iduser",null)
        dpRef  = FirebaseDatabase.getInstance().getReference("cart").child(iduser.toString())

        val phone = binding.etPhoneNumber.text
        val username = binding.etUserName.text
        val address = binding.etAddress.text
        val note = binding.etNote.text
        val email = binding.etGmail.text
//        date
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val date = currentDateTime.format(formatter)
//
        dpRef1 =  FirebaseDatabase.getInstance().getReference("order")
        dpRef2 =  FirebaseDatabase.getInstance().getReference("orderDetail")
        val total = intent.getStringExtra("total")

        val id_order = dpRef1.push().key!!

//        order_detail
        dpRef2 = FirebaseDatabase.getInstance().getReference("cart")
        dpRef3 = FirebaseDatabase.getInstance().getReference("orderDetail")

        if (iduser != null) {
            val idUser = sharedPreferences.getString("iduser", "").toString()
            val query = dpRef2.child(idUser).orderByChild("id_cart")
            var countProduct = 0

            query.addListenerForSingleValueEvent(object : ValueEventListener {

                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        for (cartSnap in snapshot.children) {
                            val cartData = cartSnap.getValue(cart::class.java)
                            if (cartData != null) {
                                countProduct += cartData.soluong!!
                                val daban = cartData.soluong

                    // cập nhật số lượng
                                dpDaban = FirebaseDatabase.getInstance().getReference("sanpham")
                                val query = dpDaban.orderByChild("id_SanPham").equalTo(cartData.id_SP)
                                query.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            for (userSnapshot in snapshot.children) {
                                                val product = userSnapshot.getValue(Sanpham::class.java)
                                            dpDaban1 = FirebaseDatabase.getInstance().getReference("sanpham").child(cartData.id_SP.toString())
                                                val daban = product?.sl_daban!!.toInt() + cartData.soluong!!.toInt()
                                                dpDaban1.child("sl_daban").setValue(daban)

                                                dpDaban1.child("soluongSP").setValue(product.soluongSP?.minus(
                                                    cartData.soluong!!
                                                ))
                                            }

                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                            }
                            val orderdetailDpref =  dpRef3.push()
                            val orderDetail = orderDetail(id_order,cartData?.id_SP, cartData?.name,
                                cartData?.soluong?.toInt(),
                                cartData?.giaSP?.times(cartData?.soluong!!), cartData?.image.toString()
                            )
                            orderdetailDpref.setValue(orderDetail)
                        }

                    }
                    val orderr = order(id_order,iduser,email.toString(),phone.toString(),address.toString(),
                        username.toString(),note.toString(),
                        total, countProduct
                        ,date,"Pending Approval")
                    dpRef1.child(id_order).setValue(orderr)
                    val builder = AlertDialog.Builder(this@Payment)
                    builder.setTitle("Notification")
                    builder.setMessage("Order successful. Waiting for accpect")
                    builder.setPositiveButton("Agree") { dialog, which ->
                val intent = Intent(this@Payment, nav_bottom::class.java)
                        startActivity(intent)
                    }
                    builder.show()

                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors here
                }
            })
            dpRef.removeValue()

        }


    }


    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->

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