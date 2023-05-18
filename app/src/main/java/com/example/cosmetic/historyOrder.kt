package com.example.cosmetic

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosmetic.Model.Sanpham
import com.example.cosmetic.Model.order
import com.example.cosmetic.adapter.orderAdapter
import com.example.cosmetic.adapter.sanphamAdapter
import com.example.cosmetic.databinding.ActivityHistoryOrderBinding
import com.google.firebase.database.*

class historyOrder : AppCompatActivity(){
    private lateinit var binding: ActivityHistoryOrderBinding
    private lateinit var Order:ArrayList<order>
    private lateinit var dpRef: DatabaseReference
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcvOrder.layoutManager = LinearLayoutManager(this)
        binding.rcvOrder.setHasFixedSize(true)
        Order = arrayListOf<order>()
        getSanPham()

    }

    private fun getSanPham() {
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences.getString("iduser",null)
        dpRef = FirebaseDatabase.getInstance().getReference("order")
        val query = dpRef.orderByChild("id_user").equalTo(iduser)
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Order.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val spData = empSnap.getValue(order::class.java)
                        Order.add(spData!!)
                    }
                    val madapter = orderAdapter(Order,this@historyOrder)
//                    binding.recycleview.adapter = sanphamAdapter(sanpham)
                    binding.rcvOrder.adapter = madapter

//        binding.rvDemo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.rcvOrder.layoutManager  = GridLayoutManager(this@historyOrder,1, GridLayoutManager.VERTICAL, false)
//                        GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                    madapter.setOnItemClickListener(object :orderAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@historyOrder,detailOrder::class.java )
                            intent.putExtra("id_order", Order[position].id_order)
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
