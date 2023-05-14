package com.example.cosmetic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.cosmetic.adapter.sanphamAdapter
import com.example.cosmetic.Model.Sanpham
import com.example.cosmetic.Model.danhmucSP
import com.example.cosmetic.Model.user
import com.example.cosmetic.adapter.SlideShow
import com.example.cosmetic.adapter.categoryAdapter
import com.example.cosmetic.databinding.FragmentFrHomeBinding



import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fr_home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fr_home : Fragment() {
    private var binding: FragmentFrHomeBinding? = null
    private lateinit var sanpham:ArrayList<Sanpham>
    private lateinit var danhmuc:ArrayList<danhmucSP>
    private lateinit var spdanhmuc:ArrayList<Sanpham>
    private lateinit var dpRef:DatabaseReference
    private lateinit var dpRef1:DatabaseReference
    private lateinit var viewPager :ViewPager2
    private  var mmAdapter:sanphamAdapter?=null
    @SuppressLint("SuspiciousIndentation")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFrHomeBinding.inflate(inflater, container, false)
        sliderShow()
              mmAdapter = sanphamAdapter(ArrayList(), requireContext())
        binding!!.recyclerView2.adapter = mmAdapter
        binding!!.searchVieww.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Xử lý sự kiện khi người dùng nhấn enter hoặc nút tìm kiếm
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterList(newText)
                }
                return true
            }
        })
        return binding!!.root
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
    private fun filterList(query: String) {
        if (query.isNotBlank()) { // Kiểm tra query có độ dài > 0
            val fiterList = ArrayList<Sanpham>()
            for (item in sanpham) {
                item.TenSP?.toLowerCase(Locale.ROOT)?.let {
                    if (it.contains(query.toLowerCase(Locale.ROOT))) {
                        fiterList.add(item)
                    }
                }
            }
//            thongbao(fiterList.size.toString())
            if(fiterList.isEmpty()){
                thongbao("Không tìm thấy")
            } else {
//                thongbao(fiterList.size.toString())
                mmAdapter?.setFilter(fiterList)
            }
        } else {
            mmAdapter?.setFilter(sanpham)
//            thongbao(sanpham.size.toString())// Hiển thị lại danh sách ban đầu khi query rỗng
        }
    }
    private fun sliderShow() {
        viewPager= binding!!.viewPager
        val images = listOf(
            R.drawable.item_slide1,
            R.drawable.item_slide2,
            R.drawable.item_slide3,
        )
        viewPager.adapter = SlideShow(images)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.offscreenPageLimit = 1

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            var currentItem = viewPager.currentItem
            currentItem += 1
            if (currentItem >= images.size) {
                currentItem = 0
            }
            viewPager.currentItem = currentItem
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 3000, 3000)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerView2?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerView2?.setHasFixedSize(true)
        sanpham = arrayListOf<Sanpham>()
        danhmuc = arrayListOf<danhmucSP>()
        spdanhmuc = arrayListOf<Sanpham>()
        getSanPham()
        getcategory()

    }

    private fun getcategory() {
        dpRef1 = FirebaseDatabase.getInstance().getReference("danhmuc")
        dpRef1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                danhmuc.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(danhmucSP::class.java)
                        danhmuc.add(empData!!)
                    }
                    val madapter = categoryAdapter(danhmuc,requireContext())
//
                    binding?.RcvCategory?.adapter = madapter

                    binding?.RcvCategory?.layoutManager =
                        GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

                    madapter.setOnItemClickListener(object : categoryAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val name_category = danhmuc[position].ten_danhmuc.toString()

                            val query = dpRef.orderByChild("id_danhmuc").equalTo("2")
                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    spdanhmuc.clear()
                                    if (snapshot.exists()) {

                                        for (userSnapshot in snapshot.children) {
                                            val sanpham = userSnapshot.getValue(Sanpham::class.java)
                                        spdanhmuc.add(sanpham!!)

                                        }

                                        val madapter = sanphamAdapter(spdanhmuc,requireContext())

                                        binding?.recyclerView2?.adapter = madapter

                                        binding?.recyclerView2?.layoutManager =
                                            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

                                        madapter.setOnItemClickListener(object : sanphamAdapter.onItemClickListener {
                                            override fun onItemClick(position: Int) {
                                                val intent = Intent(requireContext(), detail::class.java)
                                                intent.putExtra("idSP", sanpham[position].id_SanPham)
                                                intent.putExtra("TenSP", sanpham[position].TenSP)
                                                intent.putExtra("GiaSP", sanpham[position].GiaSP?.toFloat() ?: 0)
                                                intent.putExtra("SoluongSP", sanpham[position].SoluongSP?.toInt() ?: 0)
                                                intent.putExtra("AnhSP", sanpham[position].AnhSP)
                                                intent.putExtra("Id_danhmuc", sanpham[position].id_danhmuc)
                                                intent.putExtra("NhacungcapSP", sanpham[position].NhacungcapSP)
                                                intent.putExtra("MotaSP", sanpham[position].MotaSP)
                                                intent.putExtra("sl_daban", sanpham[position].sl_daban)
                                                intent.putExtra("date", sanpham[position].date)
                                                startActivity(intent)
                                            }
                                        })
                                    } else {
                                        thongbao("Danh mục trống")

                                    }


                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    fun getSanPham() {
        dpRef = FirebaseDatabase.getInstance().getReference("sanpham")
        dpRef1 = FirebaseDatabase.getInstance().getReference("danhmuc")

        dpRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sanpham.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val spData = empSnap.getValue(Sanpham::class.java)
                        val id_danhmuc = spData?.id_danhmuc
                        if (id_danhmuc != null) {
                            dpRef1.child(id_danhmuc)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Retrieve the category name and set it on the product
                                            val categoryName =
                                                dataSnapshot.child("ten_danhmuc").value.toString()
                                            spData.id_danhmuc = categoryName
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle errors here
                                    }
                                })
                        }
                        sanpham.add(spData!!)
                    }
                    val madapter = sanphamAdapter(sanpham,requireContext())

                    binding?.recyclerView2?.adapter = madapter

                    binding?.recyclerView2?.layoutManager =
                        GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

                    madapter.setOnItemClickListener(object : sanphamAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireContext(), detail::class.java)
                            intent.putExtra("idSP", sanpham[position].id_SanPham)
                            intent.putExtra("TenSP", sanpham[position].TenSP)
                            intent.putExtra("GiaSP", sanpham[position].GiaSP?.toFloat() ?: 0)
                            intent.putExtra("SoluongSP", sanpham[position].SoluongSP?.toInt() ?: 0)
                            intent.putExtra("AnhSP", sanpham[position].AnhSP)
                            intent.putExtra("Id_danhmuc", sanpham[position].id_danhmuc)
                            intent.putExtra("NhacungcapSP", sanpham[position].NhacungcapSP)
                            intent.putExtra("MotaSP", sanpham[position].MotaSP)
                            intent.putExtra("sl_daban", sanpham[position].sl_daban)
                            intent.putExtra("date", sanpham[position].date)
                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })
    }

    companion object {
        fun newInstance(): Fr_home= Fr_home()
    }
}