package com.example.cosmetic

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.cosmetic.Model.Sanpham
import com.example.cosmetic.Model.user
import com.example.cosmetic.adapter.SlideShow
import com.example.cosmetic.adapter.slideShow2
import com.example.cosmetic.databinding.ActivityDetailBinding
import com.example.cosmetic.db.countProduct
import com.google.firebase.database.*

import java.text.NumberFormat
import java.util.*


class detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewPager: ViewPager2
    private lateinit var dpRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences =
            getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        setSanPham()
        sliderShow()
        binding.btnBack.setOnClickListener {
            val intent = Intent(this@detail, nav_bottom::class.java)
            startActivity(intent)
        }

        binding.btnOrder.setOnClickListener {
            val countProduct = countProduct(this)
            val re = countProduct.addproduct(intent.getStringExtra("idSP").toString(),intent.getStringExtra("TenSP").toString(),
                intent.getFloatExtra("GiaSP", 0.0f).toFloat(),intent.getStringExtra("AnhSP").toString(),
                binding.txtSl.text.toString().toInt()
            )

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

    private fun sliderShow() {
        dpRef = FirebaseDatabase.getInstance().getReference("sanpham")
        var simage =""
        var simage2 = ""
        val query = dpRef.orderByChild("id_SanPham").equalTo(intent.getStringExtra("idSP"))
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val spData = empSnap.getValue(Sanpham::class.java)
                        simage = spData!!.anhSP.toString()
                        simage2 = spData!!.anhSP2.toString()
                    }

                }
                viewPager = binding!!.viewPager
                val bytes = Base64.decode(simage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val bytes2 = Base64.decode(simage2, Base64.DEFAULT)
                val bitmap2 = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.size)

                val images = listOf(
                    bitmap,
                    bitmap2,
                )
                viewPager.adapter = slideShow2(images)

                viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                viewPager.offscreenPageLimit = 1

                    val handler = Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        var currentItem = viewPager.currentItem
                        currentItem += 1
                        if (currentItem >= images.size) {
                            currentItem = 0
                        }
                        viewPager.currentItem = currentItem
                    }
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            handler.post(runnable)
                        }
                    }, 3000, 3000)
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })



    }



    private fun thongbao(thongbao: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
            // Xử lý sự kiện khi người dùng nhấn nút "Đồng ý"
        }
//        builder.setNegativeButton("Hủy bỏ") { dialog, which ->
//            // Xử lý sự kiện khi người dùng nhấn nút "Hủy bỏ"
//        }
        builder.show()
    }

    private fun setSanPham() {
        dpRef = FirebaseDatabase.getInstance().getReference("sanpham")
        val query = dpRef.orderByChild("id_SanPham").equalTo(intent.getStringExtra("idSP"))
        var mota = ""
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val product = userSnapshot.getValue(Sanpham::class.java)
                        if (product != null) {
                            mota = product.motaSP!!
                        }
                    }
                    binding.Tensp.text = intent.getStringExtra("TenSP")
                    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
                    val formattedPrice = numberFormat.format(intent.getFloatExtra("GiaSP", 0f))
                    binding.giasp.text = formattedPrice
//        binding.mota.text = intent.getStringExtra("MotaSP")
                    binding.mota.text = mota
                    val bytes = Base64.decode(intent.getStringExtra("AnhSP"), Base64.DEFAULT)
                    val bitMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




    }
}