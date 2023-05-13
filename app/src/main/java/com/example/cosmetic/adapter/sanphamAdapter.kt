package com.example.cosmetic.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cosmetic.Model.Sanpham
import com.example.cosmetic.R
import com.example.cosmetic.db.countProduct
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class sanphamAdapter(private var sanpham: ArrayList<Sanpham>, private val context: Context): RecyclerView.Adapter<sanphamAdapter.ViewHolder>() {
    private lateinit var mListener: onItemClickListener
    private var originalSanpham: ArrayList<Sanpham> = ArrayList()
    interface onItemClickListener  {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        originalSanpham.addAll(sanpham)
        mListener = clickListener
    }
        class ViewHolder(itemView: View, clickListener: onItemClickListener):RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
        val image =itemView.findViewById<ImageView>(R.id.imageView)
        val tensanpham =itemView.findViewById<TextView>(R.id.tenSP)
        val danhmuc =itemView.findViewById<TextView>(R.id.danhmucSP)
        //            val danhgia =findViewById<TextView>(R.id.danhgia)
        val giaSp =itemView.findViewById<TextView>(R.id.giaSP)
            val sldaban =itemView.findViewById<TextView>(R.id.sldaban)
            val add =itemView.findViewById<Button>(R.id.addGioHang)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        xml->view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_product_design,parent,false)
            return ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val cur = sanpham[position]
//        Log.d("sanphamAdapter", "TenSP: ${sanpham.TenSP}")
//        thongbao("TenSP: ${cur.TenSP}")
        holder.tensanpham.setText(cur.TenSP.toString())
        holder.danhmuc.setText(cur.id_danhmuc.toString())
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

// Chuyển đổi giá trị float thành định dạng tiền tệ
        val formattedPrice = numberFormat.format(cur.GiaSP)
        holder.giaSp.setText(formattedPrice)
        holder.sldaban.setText(cur.sl_daban.toString())
        val bytes=Base64.decode(cur.AnhSP ,Base64.DEFAULT)
        val bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.image.setImageBitmap(bitMap)


        holder.add.setOnClickListener {
            val countProduct = countProduct(context)
            val re = countProduct.addproduct(cur.id_SanPham.toString(), cur.TenSP.toString(),
                cur.GiaSP!!, cur.AnhSP.toString(), 1
            )



        }

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


    override fun getItemCount(): Int {
        return sanpham.size
    }
//    fun filterData(query: String) {
//        if (query.isEmpty()) {
//            // Nếu chuỗi tìm kiếm rỗng, hiển thị lại toàn bộ dữ liệu ban đầu
//            sanpham.clear()
//            sanpham.addAll(originalSanpham)
//        } else {
//            // Nếu chuỗi tìm kiếm không rỗng, lọc dữ liệu theo chuỗi tìm kiếm
//            val filteredList = ArrayList<Sanpham>()
//            for (item in originalSanpham) {
//                if (item.TenSP?.toLowerCase()?.contains(query.toLowerCase()) == true) {
//                    filteredList.add(item)
//                }
//            }
//            sanpham.clear()
//            sanpham.addAll(filteredList)
//        }
//        notifyDataSetChanged()
//    }

    fun setFilter(mList:ArrayList<Sanpham>){
        sanpham.clear() // Xóa tất cả các sản phẩm hiện có trong danh sách
        sanpham.addAll(mList) // Thêm danh sách sản phẩm mới vào danh sách
        notifyDataSetChanged()
    }
}