package com.example.cosmetic.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cosmetic.Model.danhmucSP
import com.example.cosmetic.R
import com.example.cosmetic.db.countProduct


import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class categoryAdapter(private var danhmuc:ArrayList<danhmucSP>, private val context: Context): RecyclerView.Adapter<categoryAdapter.ViewHolder>() {
    private lateinit var mListener: onItemClickListener
    interface onItemClickListener  {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    class ViewHolder(itemView: View, clickListener: onItemClickListener):RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
        val image =itemView.findViewById<ImageView>(R.id.image_category)
        val tendanhmuc =itemView.findViewById<TextView>(R.id.name_category)
//        val mota =itemView.findViewById<TextView>(R.id.mota)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        xml->view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rcv_item_category,parent,false)
        return ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cur = danhmuc[position]
        holder.tendanhmuc.setText(cur.ten_danhmuc.toString())
        val bytes=Base64.decode(cur.image,Base64.DEFAULT)
        val bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.image.setImageBitmap(bitMap)


    }



    override fun getItemCount(): Int {
        return danhmuc.size
    }


}