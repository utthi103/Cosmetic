package com.example.cosmetic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.example.cosmetic.Model.cart
import com.example.cosmetic.databinding.ActivityDetailBinding
import com.example.cosmetic.db.countProduct
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.text.NumberFormat
import java.util.*


class detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences =
            getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        setSanPham()

        binding.btnOrder.setOnClickListener {
            val countProduct = countProduct(this)
            val re = countProduct.addproduct(intent.getStringExtra("idSP").toString(),intent.getStringExtra("TenSP").toString(),
                intent.getFloatExtra("GiaSP", 0.0f).toFloat(),intent.getStringExtra("AnhSP").toString(),
                binding.txtSl.text.toString().toInt()
            )
//           addProduct()
//            intent.getStringExtra("idSP").toString(),
//            intent.getStringExtra("TenSP").toString(),
//            intent.getStringExtra("GiaSP")?.toFloat(),
//            intent.getStringExtra("TenSP").toString(),
//            intent.getStringExtra("AnhSP")ring().toSt
//            1

        }
        binding.btnMinus.setOnClickListener {
            var sl = binding.txtSl.text.toString().toInt()
            sl = sl - 1
            if (sl <= 0) {
                sl = 1
            }
            binding.txtSl.text = sl.toString()
        }

        binding.btnPlus.setOnClickListener {
            var sl = binding.txtSl.text.toString().toInt()
            sl = sl + 1
            val countProduct = countProduct(this)
            val count = countProduct.countProduct(intent.getStringExtra("idSP").toString()) { productCount ->
                if(sl>productCount){
                    sl = productCount
                }
                binding.txtSl.text = sl.toString()

            }
        }
    }
//
//    private fun check(): Any {
//
//    }

    private fun addProduct() {
        val id_sp = intent.getStringExtra("idSP").toString()
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences.getString("iduser",null)
//            holder.giaSp.setText(iduser.toString())
        val db = FirebaseDatabase.getInstance().getReference("cart")
        val id_cart = db.push().key!!
        val cart1 = cart(id_cart,iduser.toString(),id_sp, intent.getStringExtra("TenSP").toString(),
            intent.getFloatExtra("GiaSP", 0.0f).toFloat(),intent.getStringExtra("AnhSP").toString(),
            1)
        var count = 0;
        if(iduser!=null){
            val query = db.child(iduser)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val checkProduct = db.child(iduser).orderByChild("id_cart")

                        checkProduct.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){
                                    var check = false

                                    for (cartSnap in snapshot.children) {
                                        val cartData = cartSnap.getValue(cart::class.java)
                                        if (cartData?.id_SP.equals(intent.getStringExtra("idSP").toString() )){
                                            check = true
                                            count++
                                            break
                                        }
                                    }
                                    if (count==0) {
//                                            chưa tồn tại
//                                            holder.giaSp.setText(cur.id_SanPham.toString())
                                        db.child(iduser.toString()).child(id_cart).setValue(cart1)
//                                        thongbao("Đã thêm sản p hẩm vào giỏ hàng")


                                    } else {
//                                            holder.giaSp.setText(iduser.toString())
//                                        thongbao("Sản phẩm đã tồn tại trong giỏ hàng của bạn")

                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    } else {
                        db.child(iduser.toString()).child(id_cart).setValue(cart1)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                }
            })
        }
    }

    private fun thongbao(thongbao: String) {
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

    private fun setSanPham() {
        binding.Tensp.text = intent.getStringExtra("TenSP")
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val formattedPrice = numberFormat.format(intent.getFloatExtra("GiaSP", 0f))
        binding.giasp.text = formattedPrice
        binding.mota.text = intent.getStringExtra("MotaSP")
        val bytes = Base64.decode(intent.getStringExtra("AnhSP"), Base64.DEFAULT)
        val bitMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        binding.imageView2.setImageBitmap(bitMap)



    }
}