package com.example.cosmetic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cosmetic.Model.cart
import com.example.cosmetic.adapter.cartAdapter
import com.example.cosmetic.databinding.FragmentFrCartBinding

import com.google.firebase.database.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class Fr_cart : Fragment() {
    private var binding: FragmentFrCartBinding? = null
    private lateinit var cart1: ArrayList<cart>
    private lateinit var dpRef: DatabaseReference
    private var  total= BigDecimal.ZERO

    @SuppressLint("SuspiciousIndentation")
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFrCartBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recycleviewCart?.layoutManager = LinearLayoutManager(context)
        binding?.recycleviewCart?.setHasFixedSize(true)
        cart1 = arrayListOf<cart>()
        totalPrice()
        getSanPham()
        binding?.payment1?.setOnClickListener {
            payment()
        }
        binding?.payment2?.setOnClickListener {
            payment()
        }

    }

    private fun totalPrice() {
//         var total = BigDecimal.ZERO
        dpRef = FirebaseDatabase.getInstance().getReference("cart")
        val sharedPreferences =
            context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser", null)
        if (iduser != null) {
            val query = dpRef.child(iduser).orderByChild("id_cart")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    total = BigDecimal.ZERO // reset lại tổng giá tiền

                    for (cartSnap in snapshot.children) {
                        val cartData = cartSnap.getValue(cart::class.java)
                        if (cartData != null) {
                            val price = BigDecimal(cartData.giaSP.toString())
                            val quantity = BigDecimal(cartData.soluong.toString())
                            total = total.add(price.multiply(quantity))
                        }
                    }

                    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
                    val formattedPrice = numberFormat.format(total)

                    binding?.total?.text = formattedPrice
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun getSanPham() {
        dpRef = FirebaseDatabase.getInstance().getReference("cart")
        val sharedPreferences =
            context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser", null)
        if (iduser != null) {
            val idUser = sharedPreferences.getString("iduser", "").toString()
            val query = dpRef.child(idUser).orderByChild("id_cart")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cart1.clear()
                    if (snapshot.exists()) {
                        for (cartSnap in snapshot.children) {
                            val cartData = cartSnap.getValue(cart::class.java)

                            cart1.add(cartData!!)
                        }
//                        Toast.makeText(context, cart1.size.toString(), Toast.LENGTH_SHORT).show()
                        val madapter = context?.let { cartAdapter(cart1, it) }

                        binding?.recycleviewCart?.adapter = madapter

                        binding?.recycleviewCart?.layoutManager =
                            GridLayoutManager(
                                context,
                                1,
                                GridLayoutManager.VERTICAL,
                                false
                            )
                    }else{
                        context?.let { thongbao("Cart is empty", it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors here
                }
            })
        }

    }
    private fun thongbao(thongbao:String, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
            val intent = Intent(requireContext(), Welcome::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Xử lý sự kiện khi người dùng nhấn nút "Hủy bỏ"
        }
        builder.show()
    }
    fun payment() {
        val intent = Intent(requireActivity(), Payment::class.java)
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        intent.putExtra("total", numberFormat.format(total))
        startActivity(intent)
        requireActivity().finish()

    }

    companion object {
        fun newInstance(): Fr_cart = Fr_cart()
    }
}


