package com.example.cosmetic

import com.example.cosmetic.Login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cosmetic.Model.cart
import com.example.cosmetic.Model.user
import com.example.cosmetic.adapter.cartAdapter
import com.example.cosmetic.databinding.FragmentFrCartBinding
import com.example.cosmetic.databinding.FragmentFrUserBinding

import com.google.firebase.database.*
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class Fr_user : Fragment() {
    private var binding: FragmentFrUserBinding? = null
    private lateinit var cart1: ArrayList<cart>
    private lateinit var dpRef: DatabaseReference
    var simage: String? = ""

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
//        Edit profile
        binding!!.editprofile.setOnClickListener {
            val intent = Intent(requireContext(), editProfile::class.java)
            startActivity(intent)

        }
//        Change Picture
        binding!!.changePic.setOnClickListener {
            change_pic()
        }
        check_image()
//        load name
        setname()
//        changePass
        binding!!.changePass.setOnClickListener {
            val intent = Intent(requireContext(), Change_pass::class.java)
//            intent.putExtra("key", "value")
            startActivity(intent)
        }
//historyOrder
        binding!!.hisOrder.setOnClickListener {
            val intent = Intent(requireContext(), historyOrder::class.java)
            startActivity(intent)
//            thongbao("xsd")
        }
//        logout
        binding!!.logout.setOnClickListener {


            val builder = AlertDialog.Builder(context)
            builder.setTitle("Notification")
            builder.setMessage("Do you want to log out")
            builder.setPositiveButton("Agree") { dialog, which ->
                val sharedPreferences = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                if (sharedPreferences != null) {
                    sharedPreferences.edit().putString("iduser", null).apply()
                    val intent = Intent(requireActivity(), Login::class.java)
                    startActivity(intent)
                }
            }
            builder.setNegativeButton("Cancel") { dialog, which ->

            }
            builder.show()
        }

        return binding!!.root
    }

    private fun setname() {
        val sharedPreferences = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser",null)
        dpRef = FirebaseDatabase.getInstance().getReference("user").child(iduser.toString())
        dpRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(user::class.java)

                    val email = user?.email
                  binding?.nameuser?.text = email


            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })

    }
    fun check_image() {
        val sharedPreferences = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser",null)
        dpRef = FirebaseDatabase.getInstance().getReference("user").child(iduser.toString())
        dpRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(user::class.java)
                if (user != null) {
                    val imageUrl = user.image
                    if(imageUrl!=""){
                        val bytes=Base64.decode(imageUrl ,Base64.DEFAULT)
                        val bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                        // Load ảnh từ đường dẫn và hiển thị trên ImageView bằng Glide
                        binding?.let {
                            Glide.with(requireContext())
                                .load(bitMap)
                                .into(it.imgView)
                        }
                    }



                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
    }

    private fun confirm() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Notification")
        builder.setMessage("Do you want to change your picture")
        builder.setPositiveButton("Agree") { dialog, which ->
//        id user
            val sharedPreferences = context?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val iduser = sharedPreferences?.getString("iduser",null)
            dpRef = FirebaseDatabase.getInstance().getReference("user").child(iduser.toString())
            dpRef.child("image").setValue(simage).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                  thongbao("Change picture successful")
                } else {
                }
            }




        }

        builder.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cart1 = arrayListOf<cart>()

    }

    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
        }
        builder.show()
    }

    private fun change_pic() {
        val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)
    }
    private val ActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){
            result: ActivityResult ->
        if (result.resultCode== AppCompatActivity.RESULT_OK){
            val uri = result.data!!.data
            try {
                val inputStrem = requireActivity().contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStrem)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                simage = Base64.encodeToString(bytes, Base64.DEFAULT)
                binding?.imgView?.setImageBitmap(myBitmap)
                inputStrem!!.close()
                confirm()


            }catch (ex:java.lang.Exception){
                thongbao(ex.message.toString())
            }
        }
    }

    companion object {
        fun newInstance(): Fr_user = Fr_user()
    }

}


