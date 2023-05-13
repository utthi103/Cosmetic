package com.example.cosmetic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosmetic.Model.cart
import com.example.cosmetic.adapter.cartAdapter
import com.example.cosmetic.databinding.FragmentFrCartBinding
import com.example.cosmetic.databinding.FragmentFrUserBinding

import com.google.firebase.database.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class Fr_user : Fragment() {
    private var binding: FragmentFrUserBinding? = null
    private lateinit var cart1: ArrayList<cart>
    private lateinit var dpRef: DatabaseReference

    @SuppressLint("SuspiciousIndentation")
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFrUserBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        binding!!.editprofile.setOnClickListener {
          
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cart1 = arrayListOf<cart>()

    }

    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(context)
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

//    private fun getSanPham() {
//        dpRef = FirebaseDatabase.getInstance().getReference("cart")
//        val sharedPreferences =
//            context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
//        val iduser = sharedPreferences?.getString("iduser", null)
//        if (iduser != null) {
//            val idUser = sharedPreferences.getString("iduser", "").toString()
//            val query = dpRef.child(idUser).orderByChild("id_cart")
//            query.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    cart1.clear()
//                    if (snapshot.exists()) {
//                        for (cartSnap in snapshot.children) {
//                            val cartData = cartSnap.getValue(cart::class.java)
//                            cart1.add(cartData!!)
//                        }
////                        Toast.makeText(context, cart1.size.toString(), Toast.LENGTH_SHORT).show()
//                        val madapter = cartAdapter(cart1, requireContext())
//
//                        binding?.recycleviewCart?.adapter = madapter
//
//                        binding?.recycleviewCart?.layoutManager =
//                            GridLayoutManager(
//                                requireContext(),
//                                1,
//                                GridLayoutManager.VERTICAL,
//                                false
//                            )
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle errors here
//                }
//            })
//        }
//
//    }
}


