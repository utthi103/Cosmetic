package com.example.cosmetic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cosmetic.Model.order
import com.example.cosmetic.Model.orderDetail
import com.example.cosmetic.R
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class orderDetailAdapter  (private var OrderDetail:ArrayList<orderDetail>, private val context: Context): RecyclerView.Adapter<orderDetailAdapter.ViewHolder>() {
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener  {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    class ViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
        val name =itemView.findViewById<TextView>(R.id.name)
        val soluong =itemView.findViewById<TextView>(R.id.count)
        val imagee =itemView.findViewById<ImageView>(R.id.image)
        val total =itemView.findViewById<TextView>(R.id.total)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        xml->view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.detail_history_design,parent,false)
        return ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cur = OrderDetail[position]
        holder.name.setText(cur.name_product.toString())
        holder.soluong.setText(cur.count.toString())
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val formattedPrice = numberFormat.format(cur.price)
        holder.total.setText(formattedPrice)
        val bytes= Base64.decode(cur.image , Base64.DEFAULT)
        val bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.imagee.setImageBitmap(bitMap)


    }



    override fun getItemCount(): Int {
        return OrderDetail.size
    }


}