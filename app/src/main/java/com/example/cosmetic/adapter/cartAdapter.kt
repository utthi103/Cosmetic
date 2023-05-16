package com.example.cosmetic.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cosmetic.Model.cart
import com.example.cosmetic.R
import com.example.cosmetic.db.countProduct
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class cartAdapter(private val cart:ArrayList<cart>,private val context: Context):RecyclerView.Adapter<cartAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tenSP = itemView.findViewById<TextView>(R.id.tensp)
        val giaSP = itemView.findViewById<TextView>(R.id.giasp)
        val sl = itemView.findViewById<TextView>(R.id.txt_sl)
        val image =itemView.findViewById<ImageView>(R.id.anhsp)
        val delete = itemView.findViewById<ImageButton>(R.id.delete)
        val btntru = itemView.findViewById<ImageButton>(R.id.imgTru)
        val btncong = itemView.findViewById<ImageButton>(R.id.imgCong)
    }

//    val tenSP =
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_cart_design,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return cart.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cur = cart[position]
        holder.tenSP.setText(cur.name.toString())
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val formattedPrice = numberFormat.format(cur.giaSP)
        holder.giaSP.setText(formattedPrice)
        holder.sl.setText(cur.soluong.toString())

        val bytes = if (cart[position].image != null) {
            Base64.decode(cart[position].image, Base64.DEFAULT)
        } else {
            ByteArray(0)
        }
        val bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.image.setImageBitmap(bitMap)

//        delete product to cart
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences.getString("iduser",null)
        holder.delete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Notification")

                        builder.setMessage("Do you want to delete?")
            builder.setPositiveButton("Agree") { dialog, which ->
                deleteProduct(idCart = cur.id_cart.toString(), iduser.toString())
                notifyDataSetChanged()
            }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Xử lý sự kiện khi người dùng nhấn nút "Hủy bỏ"
        }
            builder.show()

        }

        holder.btntru.setOnClickListener {
            var curSL = cur.soluong
            if (curSL==1){
                deleteProduct(idCart = cur.id_cart.toString(), iduser.toString())
            }else{
                curSL = curSL?.minus(1)
                val cart1 = cart(cur.id_cart,iduser.toString(),cur.id_SP,cur.name.toString(),cur.giaSP,cur.image.toString(),curSL)
     val update = FirebaseDatabase.getInstance().getReference("cart").child(iduser.toString()).child(cur.id_cart.toString())
update.setValue(cart1).addOnCompleteListener { task ->
    if (task.isSuccessful) {
        holder.sl.setText(cur.soluong.toString())
    } else {
    }
}
            }
        }
        holder.btncong.setOnClickListener {
            var curSL = cur.soluong
                curSL = curSL?.plus(1)
            val countProduct = countProduct(context)
                        val count = countProduct.countProduct(cur.id_SP.toString()) { productCount ->
            if (curSL!! >productCount){
                    thongbao("Quantity is too large")
            }else{
                val cart1 = cart(cur.id_cart,iduser.toString(),cur.id_SP,cur.name.toString(),cur.giaSP,cur.image.toString(),curSL)
                val update = FirebaseDatabase.getInstance().getReference("cart").child(iduser.toString()).child(cur.id_cart.toString())
                update.setValue(cart1).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        holder.sl.setText(curSL.toString())
                    } else {
                    }
            }
            }

                }

            }


    }
    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
        }
        builder.show()
    }

    private fun deleteProduct(idCart:String, iduser:String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("cart").child(iduser.toString()).child(idCart.toString())
        databaseRef.removeValue()
    }
}