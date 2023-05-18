package com.example.cosmetic.db

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.example.cosmetic.Login
import com.example.cosmetic.Model.Sanpham
import com.example.cosmetic.Model.cart
import com.google.firebase.database.*


class countProduct(private val context: Context) {
    fun tb(){
            thongbao("dsfs")
    }

    fun check_user(): Boolean {
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences.getString("iduser",null)
        if(iduser!=null){
            return true
        }else{
            return false
        }
    }
    fun countProduct(idProduct: String, onComplete: (Int) -> Unit) {
        val dp = FirebaseDatabase.getInstance().getReference("sanpham").child(idProduct)
        dp.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val product = snapshot.getValue(Sanpham::class.java)
                    val count = product?.soluongSP ?: 0
                    onComplete(count)
                } else {
                    onComplete(0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(0)
            }
        })

    }

    fun addproduct(idSP:String, tenSP:String, giaSP: Float, anhSP:String, slSP:Int  ) {
        val id_sp = idSP.toString()
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences.getString("iduser",null)
//            holder.giaSp.setText(iduser.toString())
        val db = FirebaseDatabase.getInstance().getReference("cart")
        val id_cart = db.push().key!!
        val cart1 = cart(id_cart,iduser.toString(),id_sp,tenSP,giaSP,anhSP.toString(),slSP)
        var count = 0;
        if(iduser!=null){
            val query = db.child(iduser)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val checkProduct = db.child(iduser).orderByChild("id_cart")

                        checkProduct.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){
                                    var check = false
                                    for (cartSnap in snapshot.children) {
                                        val cartData = cartSnap.getValue(cart::class.java)
                                        if (cartData?.id_SP.equals(idSP)){
                                            check = true
                                            count++
                                            break
                                        }
                                    }
                                    if (count==0) {
//                                            chưa tồn tại
//                                            holder.giaSp.setText(cur.id_SanPham.toString())
                                        db.child(iduser.toString()).child(id_cart).setValue(cart1)
                                        thongbao("Added product to cart")


                                    } else {
//                                            holder.giaSp.setText(iduser.toString())
                                        thongbao("Product is exist your cart")

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
        }else{
        }

    }

    private fun thongbao(thongbao: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
        }
        builder.show()
    }

}