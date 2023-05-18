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
import com.example.cosmetic.Model.danhmucSP
import com.example.cosmetic.Model.order
import com.example.cosmetic.R

class orderAdapter (private var order:ArrayList<order>, private val context: Context): RecyclerView.Adapter<orderAdapter.ViewHolder>() {
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
        val date =itemView.findViewById<TextView>(R.id.date)
        val soluong =itemView.findViewById<TextView>(R.id.count)
        val total =itemView.findViewById<TextView>(R.id.total)
        val status =itemView.findViewById<TextView>(R.id.status)
//        val thaotac =itemView.findViewById<TextView>(R.id.operation)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        xml->view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.histor_order_design,parent,false)
        return ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cur = order[position]
        holder.date.setText(cur.date.toString())
      holder.soluong.setText(cur.countproduct.toString())
        holder.total.setText(cur.total.toString())
        holder.status.setText(cur.status.toString())


    }



    override fun getItemCount(): Int {
        return order.size
    }


}