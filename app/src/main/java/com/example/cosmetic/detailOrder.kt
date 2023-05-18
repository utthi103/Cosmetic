package com.example.cosmetic

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosmetic.Model.order
import com.example.cosmetic.Model.orderDetail
import com.example.cosmetic.adapter.orderAdapter
import com.example.cosmetic.adapter.orderDetailAdapter
import com.example.cosmetic.databinding.ActivityDetailBinding
import com.example.cosmetic.databinding.ActivityDetailOrderBinding
import com.example.cosmetic.databinding.ActivityHistoryOrderBinding
import com.google.firebase.database.*

class detailOrder : AppCompatActivity(){
    private lateinit var binding: ActivityDetailOrderBinding
    private lateinit var OrderDetail:ArrayList<orderDetail>
    private lateinit var dpRef: DatabaseReference
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcvOrderDetail.layoutManager = LinearLayoutManager(this)
        binding.rcvOrderDetail.setHasFixedSize(true)
        OrderDetail = arrayListOf<orderDetail>()
        getSanPham()

    }

    private fun getSanPham() {
        val id_order = intent.getStringExtra("id_order")
        dpRef = FirebaseDatabase.getInstance().getReference("orderDetail")
        val query = dpRef.orderByChild("id_order").equalTo(id_order)
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                OrderDetail.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val spData = empSnap.getValue(orderDetail::class.java)
                        OrderDetail.add(spData!!)
                    }
                    val madapter = orderDetailAdapter(OrderDetail,this@detailOrder)
                    binding.rcvOrderDetail.adapter = madapter

                    binding.rcvOrderDetail.layoutManager  = GridLayoutManager(this@detailOrder,1, GridLayoutManager.VERTICAL, false)
                    madapter.setOnItemClickListener(object : orderDetailAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@detailOrder, detail::class.java)
                            intent.putExtra("idSP", OrderDetail[position].id_product)
                            intent.putExtra("AnhSP", OrderDetail[position].image)
                            intent.putExtra("TenSP", OrderDetail[position].name_product)
                            intent.putExtra("GiaSP", OrderDetail[position].price)
                            startActivity(intent)

                        }
                    })
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
